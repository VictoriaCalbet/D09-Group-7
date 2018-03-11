
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.ManagerService;
import domain.Actor;
import domain.Manager;
import domain.form.ManagerForm;

@Service
@Transactional
public class ManagerFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;


	// Constructors -----------------------------------------------------------

	public ManagerFormService() {
		super();
	}

	public ManagerForm create() {
		ManagerForm result;

		result = new ManagerForm();

		return result;
	}

	public ManagerForm createFromActor() {
		ManagerForm result;
		final Manager manager = this.managerService.findByPrincipal();

		result = new ManagerForm();
		result.setId(manager.getId());
		result.setName(manager.getName());
		result.setSurname(manager.getSurname());
		result.setEmail(manager.getEmail());
		result.setAddress(manager.getAddress());
		result.setPhone(manager.getPhone());
		result.setBirthDate(manager.getBirthDate());
		result.setVAT(manager.getVAT());
		result.setUsername(manager.getUserAccount().getUsername());

		return result;
	}

	public Manager saveFromCreate(final ManagerForm managerForm) {
		Assert.notNull(managerForm, "message.error.managerForm.null");
		Assert.isTrue(managerForm.getPassword().equals(managerForm.getRepeatedPassword()), "message.error.managerForm.password.mismatch");
		Assert.isTrue(managerForm.getAcceptTerms(), "message.error.managerForm.acceptTerms.false");
		Assert.isTrue(managerForm.getId() == 0, "message.error.managerForm.id");

		final Manager result;
		final Manager manager;
		final UserAccount userAccount;

		Assert.isTrue(!this.actorService.checkLogin(), "message.error.manager.login");

		userAccount = this.userAccountService.createComplete(managerForm.getUsername(), managerForm.getPassword(), "MANAGER");
		manager = this.managerService.create();

		manager.setName(managerForm.getName());
		manager.setSurname(managerForm.getSurname());
		manager.setEmail(managerForm.getEmail());
		manager.setAddress(managerForm.getAddress());
		manager.setPhone(managerForm.getPhone());
		manager.setBirthDate(managerForm.getBirthDate());
		manager.setVAT(managerForm.getVAT());

		manager.setUserAccount(userAccount);

		result = this.managerService.saveFromCreate(manager);

		return result;
	}

	public Manager saveFromEdit(final ManagerForm managerForm) {
		Assert.notNull(managerForm);
		Assert.isTrue(managerForm.getPassword().equals(managerForm.getRepeatedPassword()), "message.error.managerForm.password.mismatch");

		final Actor principal = this.actorService.findByPrincipal();
		UserAccount userAccount;

		Assert.isTrue(principal.getId() == managerForm.getId(), "message.error.managerForm.edit.self");

		userAccount = principal.getUserAccount();
		userAccount.setPassword(managerForm.getPassword());

		final Manager result;
		final Manager manager;

		manager = this.managerService.findByPrincipal();

		manager.setName(managerForm.getName());
		manager.setSurname(managerForm.getSurname());
		manager.setEmail(managerForm.getEmail());
		manager.setAddress(managerForm.getAddress());
		manager.setPhone(managerForm.getPhone());
		manager.setBirthDate(managerForm.getBirthDate());
		manager.setVAT(managerForm.getVAT());

		manager.setUserAccount(userAccount);

		result = this.managerService.saveFromEdit(manager);

		return result;
	}
}
