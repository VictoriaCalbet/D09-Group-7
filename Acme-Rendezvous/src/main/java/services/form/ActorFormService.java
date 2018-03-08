
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.UserService;
import domain.Actor;
import domain.Administrator;
import domain.User;
import domain.form.ActorForm;

@Service
@Transactional
public class ActorFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserService				userService;


	// Constructors -----------------------------------------------------------

	public ActorFormService() {
		super();
	}

	public ActorForm create() {
		ActorForm result;

		result = new ActorForm();

		return result;
	}

	public ActorForm createFromActor() {
		ActorForm result;
		final Actor actor = this.actorService.findByPrincipal();

		result = new ActorForm();
		result.setId(actor.getId());
		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setEmail(actor.getEmail());
		result.setAddress(actor.getAddress());
		result.setPhone(actor.getPhone());
		result.setBirthDate(actor.getBirthDate());
		result.setUsername(actor.getUserAccount().getUsername());

		return result;
	}

	public Actor saveFromCreate(final ActorForm actorForm, final String authority) {
		Assert.notNull(actorForm, "message.error.actorForm.null");
		Assert.isTrue(actorForm.getPassword().equals(actorForm.getRepeatedPassword()), "message.error.actorForm.password.mismatch");
		Assert.isTrue(actorForm.getAcceptTerms(), "message.error.actorForm.acceptTerms.false");
		Assert.isTrue(actorForm.getId() == 0, "message.error.actorForm.id");

		if (authority.equals("ADMIN")) {
			final Administrator result;
			Administrator principal;

			principal = this.administratorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"), "message.error.actorForm.admin.create.login");
			final Administrator administrator;
			UserAccount userAccount;

			userAccount = this.userAccountService.createComplete(actorForm.getUsername(), actorForm.getPassword(), authority);
			administrator = this.administratorService.create();

			administrator.setName(actorForm.getName());
			administrator.setSurname(actorForm.getSurname());
			administrator.setEmail(actorForm.getEmail());
			administrator.setAddress(actorForm.getAddress());
			administrator.setPhone(actorForm.getPhone());
			administrator.setBirthDate(actorForm.getBirthDate());
			administrator.setUserAccount(userAccount);

			result = this.administratorService.saveFromCreate(administrator);

			return result;
		} else if (authority.equals("USER")) {
			final User result;
			final User user;
			final UserAccount userAccount;

			Assert.isTrue(!this.actorService.checkLogin(), "message.error.user.login");

			userAccount = this.userAccountService.createComplete(actorForm.getUsername(), actorForm.getPassword(), "USER");
			user = this.userService.create();

			user.setName(actorForm.getName());
			user.setSurname(actorForm.getSurname());
			user.setEmail(actorForm.getEmail());
			user.setAddress(actorForm.getAddress());
			user.setPhone(actorForm.getPhone());
			user.setBirthDate(actorForm.getBirthDate());

			user.setUserAccount(userAccount);

			result = this.userService.saveFromCreate(user);

			return result;

		}

		return null;
	}
	public Actor saveFromEdit(final ActorForm actorForm, final String authority) {
		Assert.notNull(actorForm);
		Assert.isTrue(actorForm.getPassword().equals(actorForm.getRepeatedPassword()), "message.error.actorForm.password.mismatch");

		if (authority.equals("ADMIN")) {
			final Actor principal = this.actorService.findByPrincipal();
			UserAccount userAccount;

			Assert.isTrue(principal.getId() == actorForm.getId(), "message.error.actorForm.edit.self");

			userAccount = principal.getUserAccount();
			userAccount.setPassword(actorForm.getPassword());

			final Administrator result;
			final Administrator administrator;

			administrator = this.administratorService.findByPrincipal();

			administrator.setName(actorForm.getName());
			administrator.setSurname(actorForm.getSurname());
			administrator.setEmail(actorForm.getEmail());
			administrator.setAddress(actorForm.getAddress());
			administrator.setPhone(actorForm.getPhone());
			administrator.setBirthDate(actorForm.getBirthDate());
			administrator.setUserAccount(userAccount);

			result = this.administratorService.saveFromEdit(administrator);

			return result;
		} else if (authority.equals("USER")) {
			final Actor principal = this.actorService.findByPrincipal();
			UserAccount userAccount;

			Assert.isTrue(principal.getId() == actorForm.getId(), "message.error.actorForm.edit.self");

			userAccount = principal.getUserAccount();
			userAccount.setPassword(actorForm.getPassword());

			final User result;
			final User user;

			user = this.userService.findByPrincipal();

			user.setName(actorForm.getName());
			user.setSurname(actorForm.getSurname());
			user.setEmail(actorForm.getEmail());
			user.setAddress(actorForm.getAddress());
			user.setPhone(actorForm.getPhone());
			user.setBirthDate(actorForm.getBirthDate());
			user.setUserAccount(userAccount);

			result = this.userService.saveFromEdit(user);

			return result;
		}

		return null;
	}

}
