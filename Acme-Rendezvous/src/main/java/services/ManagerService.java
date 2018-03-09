
package services;

import java.util.Collection;

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
		return null;
	}

	public Manager save(final Manager manager) {
		Assert.notNull(manager);
		Manager result;

		result = this.managerRepository.save(manager);

		return result;

	}

	public Manager saveFromCreate(final Manager manager) {
		return null;
	}

	public Manager saveFromEdit(final Manager manager) {
		return null;
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
}
