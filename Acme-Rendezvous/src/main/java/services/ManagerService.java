
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Manager;

@Service
@Transactional
public class ManagerService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;


	// Constructors -----------------------------------------------------------

	public ManagerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Manager create() {
		Manager result;
		UserAccount userAccount;

		result = new Manager();
		userAccount = this.userAccountService.create("MANAGER");
		result.setServices(new HashSet<domain.Service>());
		result.setUserAccount(userAccount);

		return result;
	}

	public Manager save(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Manager result;

		result = this.managerRepository.save(manager);

		return result;

	}

	public void flush() {
		this.managerRepository.flush();
	}

	public Manager saveFromCreate(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Assert.notNull(manager.getUserAccount().getUsername(), "message.error.manager.username.null");
		Assert.notNull(manager.getUserAccount().getPassword(), "message.error.manager.password.null");

		if (manager.getBirthDate() != null)
			Assert.isTrue(manager.getBirthDate().before(new Date()), "message.error.manager.birthDate.past");

		final Manager result;

		// Check unlogged user
		Assert.isTrue(!this.actorService.checkLogin(), "message.error.manager.login");

		// Check authority
		final boolean isManager;
		isManager = this.actorService.checkAuthority(manager, "MANAGER");
		Assert.isTrue(isManager, "message.error.manager.authority.wrong");

		// Check repeated username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUsername(manager.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.manager.username.repeated");

		result = this.save(manager);

		return result;
	}

	public Manager saveFromEdit(final Manager manager) {
		Assert.notNull(manager, "message.error.manager.null");
		Assert.notNull(manager.getUserAccount().getUsername(), "message.error.manager.username.null");
		Assert.notNull(manager.getUserAccount().getPassword(), "message.error.manager.password.null");

		if (manager.getBirthDate() != null)
			Assert.isTrue(manager.getBirthDate().before(new Date()), "message.error.manager.birthDate.past");

		final Manager result;
		Manager principal;

		// Check user as principal
		principal = this.findByPrincipal();
		Assert.isTrue(principal.getId() == manager.getId(), "message.error.manager.edit.self");

		// Check authority
		final boolean isManager;
		isManager = this.actorService.checkAuthority(manager, "MANAGER");
		Assert.isTrue(isManager, "message.error.manager.authority.wrong");

		// Encoding password
		UserAccount userAccount;
		userAccount = manager.getUserAccount();
		userAccount = this.userAccountService.modifyPassword(userAccount);
		manager.setUserAccount(userAccount);

		result = this.save(manager);

		return result;
	}

	// Other business methods -------------------------------------------------

	public Collection<Manager> findAll() {
		Collection<Manager> result = null;
		result = this.managerRepository.findAll();
		return result;
	}

	public Manager findOne(final int managerId) {
		Manager result = null;
		result = this.managerRepository.findOne(managerId);
		return result;
	}

	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.managerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 2.0 - Requisito 6.2.2
	public Collection<Manager> findManagersWithMoreServicesThanAverage() {
		return this.managerRepository.findManagersWithMoreServicesThanAverage();
	}

	// Acme-Rendezvous 2.0 - Requisito 6.2.3
	public Collection<Manager> findManagersWithMoreServicesCancelled() {
		return this.managerRepository.findManagersWithMoreServicesCancelled();
	}
}
