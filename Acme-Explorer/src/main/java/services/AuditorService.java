
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;
import domain.Audit;
import domain.Auditor;
import domain.Folder;
import domain.Message;
import domain.Note;
import domain.SocialIdentity;

@Service
@Transactional
public class AuditorService {

	// Managed Repository
	@Autowired
	private AuditorRepository		auditorRepository;

	// Supporting Services
	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private FolderService			folderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MessageService			messageService;


	// Constructor

	public AuditorService() {
		super();
	}

	// Simple CRUD methods
	public Auditor create() {
		Auditor result;
		UserAccount userAccount;

		result = new Auditor();
		result.setAudits(new HashSet<Audit>());
		result.setNotes(new HashSet<Note>());

		userAccount = this.userAccountService.create("AUDITOR");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Auditor save(final Auditor auditor) {
		Assert.notNull(auditor, "message.error.auditor.null");

		Auditor result;

		result = this.auditorRepository.save(auditor);

		return result;
	}

	public Auditor saveFromCreate(final Auditor auditor) {
		Assert.notNull(auditor, "message.error.auditor.null");
		Assert.notNull(auditor.getUserAccount().getUsername(), "message.error.auditor.username");
		Assert.notNull(auditor.getUserAccount().getPassword(), "message.error.auditor.password");

		Auditor result;
		Collection<Folder> newFolders;

		// Check AUDITOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAuditor(auditor);
		Assert.isTrue(correctAuthority, "message.error.auditor.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(auditor.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.auditor.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = auditor.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		auditor.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(auditor);
		auditor.setFolders(newFolders);

		// Add CC if PN phone
		if (auditor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(auditor);
			auditor.setPhone(updatedPhone);
		}

		// Set suspicious to false
		auditor.setIsSuspicious(false);
		auditor.setIsBanned(false);

		// Set suspicious of the creator
		final Administrator principal = this.administratorService.findByPrincipal();
		boolean isSuspicious = principal.getIsSuspicious() || this.messageService.checkSpam(auditor.getName()) || this.messageService.checkSpam(auditor.getSurname()) || this.messageService.checkSpam(auditor.getEmail());

		if (auditor.getPhone() != null && auditor.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(auditor.getPhone());

		if (auditor.getAddress() != null && auditor.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(auditor.getAddress());

		principal.setIsSuspicious(isSuspicious);
		this.administratorService.saveFromEdit(principal);

		// Save auditor
		result = this.save(auditor);

		return result;
	}

	public Auditor saveFromEditWithEncoding(final Auditor auditor) {
		Assert.notNull(auditor, "message.error.auditor.null");
		Assert.notNull(auditor.getUserAccount().getUsername(), "message.error.auditor.username");
		Assert.notNull(auditor.getUserAccount().getPassword(), "message.error.auditor.password");

		Auditor result;
		Auditor principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.auditor.login");
		Assert.isTrue(principal.getId() == auditor.getId(), "message.error.auditor.principal");

		// Check AUDITOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAuditor(auditor);
		Assert.isTrue(correctAuthority, "message.error.auditor.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = auditor.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		auditor.setUserAccount(userAccount);

		// Add CC if PN phone
		if (auditor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(auditor);
			auditor.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = auditor.getIsSuspicious() || this.messageService.checkSpam(auditor.getName()) || this.messageService.checkSpam(auditor.getSurname()) || this.messageService.checkSpam(auditor.getEmail());

		if (auditor.getPhone() != null && auditor.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(auditor.getPhone());

		if (auditor.getAddress() != null && auditor.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(auditor.getAddress());

		auditor.setIsSuspicious(isSuspicious);

		// Save auditor
		result = this.save(auditor);

		return result;
	}

	public Auditor saveFromEdit(final Auditor auditor) {
		Assert.notNull(auditor);
		Assert.notNull(auditor.getUserAccount().getUsername());
		Assert.notNull(auditor.getUserAccount().getPassword());

		Auditor result;

		// Check AUDITOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAuditor(auditor);
		Assert.isTrue(correctAuthority);

		// Add CC if PN phone
		if (auditor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(auditor);
			auditor.setPhone(updatedPhone);
		}

		// Save auditor
		result = this.save(auditor);

		return result;
	}

	// Other business methods
	public Collection<Auditor> findAll() {
		Collection<Auditor> result;

		result = this.auditorRepository.findAll();

		return result;
	}

	public Auditor findOne(final int auditorId) {
		Auditor result;

		result = this.auditorRepository.findOne(auditorId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.auditorRepository.count();

		return result;
	}

	//Devuelve el auditor que esta creado
	public Auditor findByPrincipal() {
		Auditor result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.auditorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	private boolean checkAuditor(final Auditor auditor) {
		Assert.notNull(auditor);
		Assert.notNull(auditor.getUserAccount().getUsername());
		Assert.notNull(auditor.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(auditor, Authority.AUDITOR);

		return result;
	}

}
