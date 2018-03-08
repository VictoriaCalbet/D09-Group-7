
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ExplorerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Application;
import domain.ContactEmergency;
import domain.CreditCard;
import domain.Explorer;
import domain.Finder;
import domain.Folder;
import domain.Message;
import domain.SocialIdentity;
import domain.Story;
import domain.SurvivalClass;

@Service
@Transactional
public class ExplorerService {

	// Managed Repository
	@Autowired
	private ExplorerRepository	explorerRepository;

	// Supporting Services

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private MessageService		messageService;


	// Constructor

	public ExplorerService() {
		super();
	}

	// Simple CRUD methods
	public Explorer create() {
		Explorer result;
		CreditCard creditCard;
		UserAccount userAccount;

		result = new Explorer();
		creditCard = new CreditCard();
		result.setCreditCard(creditCard);

		result.setSurvivalClasses(new HashSet<SurvivalClass>());
		result.setStories(new HashSet<Story>());
		result.setApplications(new HashSet<Application>());
		result.setContactEmergencies(new HashSet<ContactEmergency>());

		userAccount = this.userAccountService.create("EXPLORER");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Explorer save(final Explorer explorer) {
		Assert.notNull(explorer, "message.error.explorer.null");

		Explorer result;

		result = this.explorerRepository.save(explorer);

		return result;
	}

	public Explorer saveApplication(final Explorer explorer) {
		Assert.notNull(explorer, "message.error.explorer.null");

		Explorer result;

		result = this.explorerRepository.saveAndFlush(explorer);

		return result;
	}

	public Explorer saveFromCreate(final Explorer explorer) {
		Assert.notNull(explorer, "message.error.explorer.null");
		Assert.notNull(explorer.getUserAccount().getUsername(), "message.error.explorer.username");
		Assert.notNull(explorer.getUserAccount().getPassword(), "message.error.explorer.password");

		Explorer result;
		Collection<Folder> newFolders;

		// Check ADMIN authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkExplorer(explorer);
		Assert.isTrue(correctAuthority, "message.error.explorer.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(explorer.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.explorer.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = explorer.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		explorer.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(explorer);
		explorer.setFolders(newFolders);

		// Add CC if PN phone
		if (explorer.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(explorer);
			explorer.setPhone(updatedPhone);
		}

		// Check CreditCard attributes.
		boolean validCreditCard;
		validCreditCard = this.actorService.checkCreditCard(explorer.getCreditCard());
		Assert.isTrue(validCreditCard, "message.error.explorer.creditCard.valid");

		// Set suspicious of the creator
		boolean isSuspicious = this.messageService.checkSpam(explorer.getName()) || this.messageService.checkSpam(explorer.getSurname()) || this.messageService.checkSpam(explorer.getEmail())
			|| this.messageService.checkSpam(explorer.getCreditCard().getHolderName()) || this.messageService.checkSpam(explorer.getCreditCard().getBrandName()) || this.messageService.checkSpam(explorer.getCreditCard().getNumber());

		if (explorer.getPhone() != null && explorer.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(explorer.getPhone());

		if (explorer.getAddress() != null && explorer.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(explorer.getAddress());

		// Set suspicious to false
		explorer.setIsSuspicious(isSuspicious);
		explorer.setIsBanned(false);

		// Save explorer
		result = this.save(explorer);

		//New finder
		Finder finder;
		finder = this.finderService.create();
		finder.setExplorer(result);
		this.finderService.save(finder);
		return result;
	}

	public Explorer saveFromEditWithEncoding(final Explorer explorer) {
		Assert.notNull(explorer, "message.error.explorer.null");
		Assert.notNull(explorer.getUserAccount().getUsername(), "message.error.explorer.username");
		Assert.notNull(explorer.getUserAccount().getPassword(), "message.error.explorer.password");

		Explorer result;
		Explorer principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.explorer.login");
		Assert.isTrue(principal.getId() == explorer.getId(), "message.error.explorer.principal");

		// Check EXPLORER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkExplorer(explorer);
		Assert.isTrue(correctAuthority, "message.error.explorer.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = explorer.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		explorer.setUserAccount(userAccount);

		// Add CC if PN phone
		if (explorer.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(explorer);
			explorer.setPhone(updatedPhone);
		}

		// Check CreditCard attributes.
		boolean validCreditCard;
		validCreditCard = this.actorService.checkCreditCard(explorer.getCreditCard());
		Assert.isTrue(validCreditCard, "message.error.explorer.creditCard.valid");

		// Set suspicious of the creator
		boolean isSuspicious = explorer.getIsSuspicious() || this.messageService.checkSpam(explorer.getName()) || this.messageService.checkSpam(explorer.getSurname()) || this.messageService.checkSpam(explorer.getEmail())
			|| this.messageService.checkSpam(explorer.getCreditCard().getHolderName()) || this.messageService.checkSpam(explorer.getCreditCard().getBrandName()) || this.messageService.checkSpam(explorer.getCreditCard().getNumber());

		if (explorer.getPhone() != null && explorer.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(explorer.getPhone());

		if (explorer.getAddress() != null && explorer.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(explorer.getAddress());

		explorer.setIsSuspicious(isSuspicious);

		// Save manager
		result = this.save(explorer);

		return result;
	}

	public Explorer saveFromEdit(final Explorer explorer) {
		Assert.notNull(explorer, "message.error.explorer.null");
		Assert.notNull(explorer.getUserAccount().getUsername(), "message.error.explorer.username");
		Assert.notNull(explorer.getUserAccount().getPassword(), "message.error.explorer.password");

		Explorer result;

		// Check EXPLORER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkExplorer(explorer);
		Assert.isTrue(correctAuthority, "message.error.explorer.authority");

		// Add CC if PN phone
		if (explorer.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(explorer);
			explorer.setPhone(updatedPhone);
		}

		// Check CreditCard attributes.
		boolean validCreditCard;
		validCreditCard = this.actorService.checkCreditCard(explorer.getCreditCard());
		Assert.isTrue(validCreditCard, "message.error.explorer.creditCard.valid");

		// Save manager
		result = this.save(explorer);

		return result;
	}

	// Other business methods
	public Collection<Explorer> findAll() {
		Collection<Explorer> result;

		result = this.explorerRepository.findAll();

		return result;
	}

	public Explorer findOne(final int explorerId) {
		Explorer result;

		result = this.explorerRepository.findOne(explorerId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.explorerRepository.count();

		return result;
	}

	public Explorer findByPrincipal() {
		Explorer result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.explorerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	public Explorer findOneByApplicationId(final int applicationId) {
		Explorer result;

		result = this.explorerRepository.findOneByApplicationId(applicationId);
		Assert.notNull(result);

		return result;
	}

	public boolean checkExplorer(final Explorer explorer) {
		Assert.notNull(explorer);
		Assert.notNull(explorer.getUserAccount().getUsername());
		Assert.notNull(explorer.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(explorer, Authority.EXPLORER);

		return result;
	}
}
