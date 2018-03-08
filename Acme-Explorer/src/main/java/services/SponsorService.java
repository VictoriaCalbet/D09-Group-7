
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;
import domain.Folder;
import domain.Message;
import domain.SocialIdentity;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorService {

	// Managed Repository
	@Autowired
	private SponsorRepository		sponsorRepository;

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

	public SponsorService() {
		super();
	}

	// Simple CRUD methods
	public Sponsor create() {
		Sponsor result;
		UserAccount userAccount;

		result = new Sponsor();
		result.setSponsorships(new HashSet<Sponsorship>());

		userAccount = this.userAccountService.create("SPONSOR");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Sponsor save(final Sponsor sponsor) {
		Assert.notNull(sponsor, "message.error.sponsor.null");

		Sponsor result;

		result = this.sponsorRepository.save(sponsor);

		return result;
	}

	public Sponsor saveFromCreate(final Sponsor sponsor) {
		Assert.notNull(sponsor, "message.error.sponsor.null");
		Assert.notNull(sponsor.getUserAccount().getUsername(), "message.error.sponsor.username");
		Assert.notNull(sponsor.getUserAccount().getPassword(), "message.error.sponsor.password");

		Sponsor result;
		Collection<Folder> newFolders;

		// Check SPONSOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkSponsor(sponsor);
		Assert.isTrue(correctAuthority, "message.error.sponsor.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(sponsor.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.sponsor.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = sponsor.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		sponsor.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(sponsor);
		sponsor.setFolders(newFolders);

		// Add CC if PN phone
		if (sponsor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(sponsor);
			sponsor.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		final Administrator admin = this.administratorService.findByPrincipal();
		boolean isSuspicious = admin.getIsSuspicious() || this.messageService.checkSpam(sponsor.getName()) || this.messageService.checkSpam(sponsor.getSurname()) || this.messageService.checkSpam(sponsor.getEmail());

		if (sponsor.getPhone() != null && sponsor.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(sponsor.getPhone());

		if (sponsor.getAddress() != null && sponsor.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(sponsor.getAddress());

		admin.setIsSuspicious(isSuspicious);
		this.administratorService.saveFromEdit(admin);

		// Set suspicious to false
		sponsor.setIsSuspicious(false);
		sponsor.setIsBanned(false);

		// Save sponsor
		result = this.save(sponsor);

		return result;
	}

	public Sponsor saveFromEditWithEncoding(final Sponsor sponsor) {
		Assert.notNull(sponsor, "message.error.sponsor.null");
		Assert.notNull(sponsor.getUserAccount().getUsername(), "message.error.sponsor.username");
		Assert.notNull(sponsor.getUserAccount().getPassword(), "message.error.sponsor.password");

		Sponsor result;
		Sponsor principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.sponsor.login");
		Assert.isTrue(principal.getId() == sponsor.getId(), "message.error.sponsor.principal");

		// Check SPONSOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkSponsor(sponsor);
		Assert.isTrue(correctAuthority, "message.error.sponsor.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = sponsor.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		sponsor.setUserAccount(userAccount);

		// Add CC if PN phone
		if (sponsor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(sponsor);
			sponsor.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = sponsor.getIsSuspicious() || this.messageService.checkSpam(sponsor.getName()) || this.messageService.checkSpam(sponsor.getSurname()) || this.messageService.checkSpam(sponsor.getEmail());

		if (sponsor.getPhone() != null && sponsor.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(sponsor.getPhone());

		if (sponsor.getAddress() != null && sponsor.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(sponsor.getAddress());

		sponsor.setIsSuspicious(isSuspicious);

		// Save manager
		result = this.save(sponsor);

		return result;
	}

	public Sponsor saveFromEdit(final Sponsor sponsor) {
		Assert.notNull(sponsor, "message.error.sponsor.null");
		Assert.notNull(sponsor.getUserAccount().getUsername(), "message.error.sponsor.username");
		Assert.notNull(sponsor.getUserAccount().getPassword(), "message.error.sponsor.password");

		Sponsor result;

		// Check SPONSOR authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkSponsor(sponsor);
		Assert.isTrue(correctAuthority, "message.error.sponsor.authority");

		// Add CC if PN phone
		if (sponsor.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(sponsor);
			sponsor.setPhone(updatedPhone);
		}

		// Save manager
		result = this.save(sponsor);

		return result;
	}

	// Other business methods
	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;

		result = this.sponsorRepository.findAll();

		return result;
	}

	public Sponsor findOne(final int sponsorId) {
		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.sponsorRepository.count();

		return result;
	}

	//Devuelve el manager que esta creado
	public Sponsor findByPrincipal() {
		Sponsor result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.sponsorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	private boolean checkSponsor(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.notNull(sponsor.getUserAccount().getUsername());
		Assert.notNull(sponsor.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(sponsor, Authority.SPONSOR);

		return result;
	}
}
