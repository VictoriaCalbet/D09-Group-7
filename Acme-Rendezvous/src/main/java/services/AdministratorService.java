
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;


	// Constructors -----------------------------------------------------------

	public AdministratorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Administrator create() {
		Administrator result;
		UserAccount userAccount;

		result = new Administrator();
		userAccount = this.userAccountService.create("ADMIN");
		result.setUserAccount(userAccount);

		return result;
	}

	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Administrator result;

		result = this.administratorRepository.save(administrator);

		return result;

	}

	public Administrator saveFromCreate(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Assert.notNull(administrator.getUserAccount().getUsername(), "message.error.administrator.username.null");
		Assert.notNull(administrator.getUserAccount().getPassword(), "message.error.administrator.password.null");

		if (administrator.getBirthDate() != null)
			Assert.isTrue(administrator.getBirthDate().before(new Date()), "message.error.administrator.birthDate.past");

		Administrator result;
		final Administrator principal;

		// Check admin as principal
		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.administrator.principal.admin");

		// Check Authority
		final boolean isAdmin;
		isAdmin = this.actorService.checkAuthority(administrator, "ADMIN");
		Assert.isTrue(isAdmin, "message.error.administrator.authority.wrong");

		// Check repeated username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUsername(administrator.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.administrator.username.repeated");

		result = this.save(administrator);

		return result;
	}

	public Administrator saveFromEdit(final Administrator administrator) {
		Assert.notNull(administrator, "message.error.administrator.null");
		Assert.notNull(administrator.getUserAccount().getUsername(), "message.error.administrator.username.null");
		Assert.notNull(administrator.getUserAccount().getPassword(), "message.error.administrator.password.null");

		if (administrator.getBirthDate() != null)
			Assert.isTrue(administrator.getBirthDate().before(new Date()), "message.error.administrator.birthDate.past");

		final Administrator result;
		final Administrator principal;

		// Check admin as principal
		principal = this.findByPrincipal();
		Assert.isTrue(principal.getId() == administrator.getId(), "message.error.administrator.edit.self");

		// Check Authority
		final boolean isAdmin;
		isAdmin = this.actorService.checkAuthority(administrator, "ADMIN");
		Assert.isTrue(isAdmin, "message.error.administrator.authority.wrong");

		// Encoding password
		UserAccount userAccount;
		userAccount = administrator.getUserAccount();
		userAccount = this.userAccountService.modifyPassword(userAccount);
		administrator.setUserAccount(userAccount);

		result = this.save(administrator);

		return result;
	}

	// Other business methods -------------------------------------------------

	public Collection<Administrator> findAll() {
		Collection<Administrator> result = null;
		result = this.administratorRepository.findAll();
		return result;
	}

	public Administrator findOne(final int administratorId) {
		Administrator result = null;
		result = this.administratorRepository.findOne(administratorId);
		return result;
	}

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.administratorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}
}
