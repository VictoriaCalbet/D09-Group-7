
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Folder;
import domain.Manager;
import domain.Message;
import domain.SocialIdentity;
import domain.Trip;

@Service
@Transactional
public class ManagerService {

	// Managed Repository
	@Autowired
	private ManagerRepository		managerRepository;

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

	public ManagerService() {
		super();
	}

	// Simple CRUD methods
	public Manager create() {
		Manager result;
		UserAccount userAccount;

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.manager.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.manager.admin");

		result = new Manager();
		result.setIsSuspicious(false);
		result.setIsBanned(false);
		result.setTrips(new HashSet<Trip>());

		userAccount = this.userAccountService.create("MANAGER");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Manager save(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");

		Manager result;

		result = this.managerRepository.save(manager);

		return result;
	}

	public Manager saveFromCreate(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Assert.notNull(manager.getUserAccount().getUsername(), "message.error.manager.username");
		Assert.notNull(manager.getUserAccount().getPassword(), "message.error.manager.password");

		Manager result;
		Collection<Folder> newFolders;

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.manager.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.manager.admin");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(manager.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.manager.username.repeated");

		// Check MANAGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkManager(manager);
		Assert.isTrue(correctAuthority, "message.error.manager.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = manager.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		manager.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(manager);
		manager.setFolders(newFolders);

		// Add CC if PN phone
		if (manager.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(manager);
			manager.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		final Administrator admin = this.administratorService.findByPrincipal();
		boolean isSuspicious = admin.getIsSuspicious() || this.messageService.checkSpam(manager.getName()) || this.messageService.checkSpam(manager.getSurname()) || this.messageService.checkSpam(manager.getEmail());

		if (manager.getPhone() != null && manager.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(manager.getPhone());

		if (manager.getAddress() != null && manager.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(manager.getAddress());

		admin.setIsSuspicious(isSuspicious);
		this.administratorService.saveFromEdit(admin);

		// Set suspicious to false
		manager.setIsSuspicious(false);
		manager.setIsBanned(false);

		// Save manager
		result = this.save(manager);

		return result;
	}

	public Manager saveFromEditWithEncoding(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Assert.notNull(manager.getUserAccount().getUsername(), "message.error.manager.username");
		Assert.notNull(manager.getUserAccount().getPassword(), "message.error.manager.password");

		Manager result;
		Manager principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.manager.login");
		Assert.isTrue(principal.getId() == manager.getId(), "message.error.manager.principal");

		// Check MANAGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkManager(manager);
		Assert.isTrue(correctAuthority, "message.error.manager.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = manager.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		manager.setUserAccount(userAccount);

		// Add CC if PN phone
		if (manager.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(manager);
			manager.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = manager.getIsSuspicious() || this.messageService.checkSpam(manager.getName()) || this.messageService.checkSpam(manager.getSurname()) || this.messageService.checkSpam(manager.getEmail());

		if (manager.getPhone() != null && manager.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(manager.getPhone());

		if (manager.getAddress() != null && manager.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(manager.getAddress());

		manager.setIsSuspicious(isSuspicious);

		// Save manager
		result = this.save(manager);

		return result;
	}

	public Manager saveFromEdit(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Assert.notNull(manager.getUserAccount().getUsername(), "message.error.manager.username");
		Assert.notNull(manager.getUserAccount().getPassword(), "message.error.manager.password");

		Manager result;

		// Check MANAGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkManager(manager);
		Assert.isTrue(correctAuthority, "message.error.manager.authority");

		// Add CC if PN phone
		if (manager.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(manager);
			manager.setPhone(updatedPhone);
		}

		// Save manager
		result = this.save(manager);

		return result;
	}

	public Manager ban(final int managerId) {
		final Manager result;
		Manager managerToBan;
		UserAccount userAccountToBan;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.manager.ban.admin");

		managerToBan = this.managerRepository.findOne(managerId);
		Assert.notNull(managerToBan, "message.error.manager.null");
		Assert.isTrue(managerToBan.getIsSuspicious(), "message.error.manager.ban.suspicious");
		managerToBan.setIsBanned(true);

		// De-activate the user account.
		userAccountToBan = managerToBan.getUserAccount();
		userAccountToBan.setIsEnabled(false);
		managerToBan.setUserAccount(userAccountToBan);

		result = this.save(managerToBan);

		return result;
	}

	public Manager unban(final int managerId) {
		Manager result;
		Manager managerToUnban;
		final UserAccount userAccountToUnban;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.manager.ban.admin");

		managerToUnban = this.managerRepository.findOne(managerId);
		Assert.notNull(managerToUnban, "message.error.manager.null");
		Assert.isTrue(managerToUnban.getIsBanned(), "message.error.manager.ban.banned");
		managerToUnban.setIsBanned(false);

		// Re-activate the user account.
		userAccountToUnban = managerToUnban.getUserAccount();
		userAccountToUnban.setIsEnabled(true);
		managerToUnban.setUserAccount(userAccountToUnban);

		result = this.save(managerToUnban);

		return result;
	}

	// Other business methods
	public Collection<Manager> findAll() {
		Collection<Manager> result;

		result = this.managerRepository.findAll();

		return result;
	}

	public Manager findOne(final int managerId) {
		Manager result;

		result = this.managerRepository.findOne(managerId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.managerRepository.count();

		return result;
	}

	public Collection<Manager> findAllSuspicious() {
		Collection<Manager> result;

		result = this.managerRepository.findAllSuspicious();

		return result;
	}

	//Devuelve el manager que esta creado
	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.managerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	public Manager findOneByApplicationId(final int applicationId) {
		Manager result;

		result = this.managerRepository.findOneByApplicationId(applicationId);
		Assert.notNull(result);

		return result;
	}

	private boolean checkManager(final Manager manager) {
		Assert.notNull(manager);
		Assert.notNull(manager.getUserAccount().getUsername());
		Assert.notNull(manager.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(manager, Authority.MANAGER);

		return result;
	}

	//C-14.6.2.1 The average number of trips managed per manager
	public Double findAverageTripsPerManager() {
		return this.managerRepository.findAverageTripsPerManager();
	}
	//C-14.6.2.2 The minimum number of trips managed per manager
	public Integer findMinTripsPerManager() {
		return this.managerRepository.findMinTripsPerManager();
	}
	//C-14.6.2.3 The maximum number of trips managed per manager
	public Integer findMaxTripsPerManager() {
		return this.managerRepository.findMaxTripsPerManager();
	}
	//C-14.6.2.4 The standard deviation of the number of trips managed per manager
	public Double findStandardDeviationOfTripsPerManager() {
		return this.managerRepository.findStandardDeviationOfTripsPerManager();
	}
	//C-16.4.6 The ratio of suspicious managers
	public Double findRatioOfSuspiciousManagers() {
		return this.managerRepository.findRatioOfSuspiciousManagers();
	}
}
