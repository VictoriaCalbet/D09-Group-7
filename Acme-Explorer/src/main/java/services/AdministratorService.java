
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;
import domain.Folder;
import domain.Message;
import domain.SocialIdentity;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting Services
	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private FolderService			folderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageService			messageService;


	// Constructor

	public AdministratorService() {
		super();
	}

	// Simple CRUD methods
	public Administrator create() {
		Administrator result;
		UserAccount userAccount;

		result = new Administrator();

		userAccount = this.userAccountService.create("ADMIN");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Administrator save(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");

		Administrator result;

		result = this.administratorRepository.save(administrator);

		return result;
	}

	public Administrator saveFromCreate(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Assert.notNull(administrator.getUserAccount().getUsername(), "message.error.administrator.username");
		Assert.notNull(administrator.getUserAccount().getPassword(), "message.error.administrator.password");

		final Administrator result;
		Collection<Folder> newFolders;

		// Check ADMIN authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAdministrator(administrator);
		Assert.isTrue(correctAuthority, "message.error.administrator.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(administrator.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.administrator.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = administrator.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		administrator.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(administrator);
		administrator.setFolders(newFolders);

		// Add CC if PN phone
		if (administrator.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(administrator);
			administrator.setPhone(updatedPhone);
		}

		// Set suspicious to false
		administrator.setIsSuspicious(false);
		administrator.setIsBanned(false);

		// Set suspicious of the creator
		final Administrator principal = this.findByPrincipal();
		boolean isSuspicious = principal.getIsSuspicious() || this.messageService.checkSpam(administrator.getName()) || this.messageService.checkSpam(administrator.getSurname()) || this.messageService.checkSpam(administrator.getEmail());

		if (administrator.getPhone() != null && administrator.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(administrator.getPhone());

		if (administrator.getAddress() != null && administrator.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(administrator.getAddress());

		principal.setIsSuspicious(isSuspicious);
		this.save(principal);

		// Save admin
		result = this.save(administrator);

		return result;
	}
	public Administrator saveFromEditWithEncoding(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Assert.notNull(administrator.getUserAccount().getUsername(), "message.error.administrator.username");
		Assert.notNull(administrator.getUserAccount().getPassword(), "message.error.administrator.password");

		Administrator result;
		Administrator principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.administrator.login");
		Assert.isTrue(principal.getId() == administrator.getId(), "message.error.administrator.principal");

		// Check ADMIN authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAdministrator(administrator);
		Assert.isTrue(correctAuthority, "message.error.administrator.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = administrator.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		administrator.setUserAccount(userAccount);

		// Add CC if PN phone
		if (administrator.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(administrator);
			administrator.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = administrator.getIsSuspicious() || this.messageService.checkSpam(administrator.getName()) || this.messageService.checkSpam(administrator.getSurname()) || this.messageService.checkSpam(administrator.getEmail());

		if (administrator.getPhone() != null && administrator.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(administrator.getPhone());

		if (administrator.getAddress() != null && administrator.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(administrator.getAddress());

		administrator.setIsSuspicious(isSuspicious);

		// Save admin
		result = this.save(administrator);

		return result;
	}

	public Administrator saveFromEdit(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Assert.notNull(administrator.getUserAccount().getUsername(), "message.error.administrator.username");
		Assert.notNull(administrator.getUserAccount().getPassword(), "message.error.administrator.password");

		Administrator result;

		// Check ADMIN authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkAdministrator(administrator);
		Assert.isTrue(correctAuthority, "message.error.administrator.authority");

		// Add CC if PN phone
		if (administrator.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(administrator);
			administrator.setPhone(updatedPhone);
		}

		// Save manager
		result = this.save(administrator);

		return result;
	}

	// Other business methods
	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();

		return result;
	}

	public Administrator findOne(final int administratorId) {
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.administratorRepository.count();

		return result;
	}

	//Devuelve el adminstrator que esta creado
	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.administratorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	private boolean checkAdministrator(final Administrator administrator) {
		Assert.notNull(administrator);
		Assert.notNull(administrator.getUserAccount().getUsername());
		Assert.notNull(administrator.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(administrator, Authority.ADMIN);

		return result;
	}

}
