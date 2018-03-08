
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Actor> findAll() {
		Collection<Actor> result = null;
		result = this.actorRepository.findAll();
		return result;
	}

	public Actor findOne(final int actorId) {
		Actor result = null;
		result = this.actorRepository.findOne(actorId);
		return result;
	}

	// Other business methods -------------------------------------------------

	public Actor findByPrincipal() {
		Actor result = null;
		UserAccount userAccount = null;

		userAccount = LoginService.getPrincipal();
		result = this.actorRepository.findByUserAccountId(userAccount.getId());

		Assert.notNull(result);

		return result;
	}

	public boolean checkAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor);

		boolean result = false;

		for (final Authority a : actor.getUserAccount().getAuthorities())
			if (a.getAuthority().equals(authority)) {
				result = true;
				break;
			}

		return result;
	}

	public boolean checkLogin() {
		boolean result;

		result = true;

		try {
			this.findByPrincipal();
		} catch (final Throwable e) {
			result = false;
		}
		return result;
	}
}
