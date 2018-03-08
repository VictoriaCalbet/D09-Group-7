
package services;

import java.util.Calendar;
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
import domain.CreditCard;
import domain.SystemConfiguration;

@Service
@Transactional
public class ActorService {

	// Managed Repository
	@Autowired
	private ActorRepository				actorRepository;

	// Supporting Services
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Constructor

	public ActorService() {
		super();
	}

	// Simple CRUD methods
	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		if (actor.getId() == 0)
			result = this.actorRepository.save(actor);
		else
			result = this.actorRepository.save(actor);

		return result;
	}

	public Actor ban(final int actorId) {
		final Actor result;
		Actor actorToBan;
		UserAccount userAccountToBan;
		Actor principal;

		principal = this.findByPrincipal();
		Assert.isTrue(this.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.actor.ban.admin");

		actorToBan = this.actorRepository.findOne(actorId);
		Assert.notNull(actorToBan, "message.error.actor.null");
		Assert.isTrue(actorToBan.getIsSuspicious(), "message.error.actor.ban.suspicious");
		actorToBan.setIsBanned(true);

		// De-activate the user account.
		userAccountToBan = actorToBan.getUserAccount();
		userAccountToBan.setIsEnabled(false);
		actorToBan.setUserAccount(userAccountToBan);

		result = this.save(actorToBan);

		return result;
	}

	public Actor unban(final int actorId) {
		Actor result;
		Actor actorToUnban;
		final UserAccount userAccountToUnban;
		Actor principal;

		principal = this.findByPrincipal();
		Assert.isTrue(this.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.actor.ban.admin");

		actorToUnban = this.actorRepository.findOne(actorId);
		Assert.notNull(actorToUnban, "message.error.actor.null");
		Assert.isTrue(actorToUnban.getIsBanned(), "message.error.actor.ban.banned");
		actorToUnban.setIsBanned(false);

		// Re-activate the user account.
		userAccountToUnban = actorToUnban.getUserAccount();
		userAccountToUnban.setIsEnabled(true);
		actorToUnban.setUserAccount(userAccountToUnban);

		result = this.save(actorToUnban);

		return result;
	}

	// Other business methods

	public boolean checkAuthority(final String authority) {
		boolean result;
		Actor actor;
		Collection<Authority> authorities;
		result = false;

		try {
			actor = this.findByPrincipal();
			authorities = actor.getUserAccount().getAuthorities();

			for (final Authority a : authorities)
				if (a.getAuthority().equalsIgnoreCase(authority)) {
					result = true;
					break;
				}
		} catch (final IllegalArgumentException e) {
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param actor
	 * @param authority
	 *            must be in the form Authority.[ROL]
	 * @return
	 */
	public boolean checkActorWithAuthority(final Actor actor, final String authority) {
		Assert.notNull(actor);
		Assert.notNull(actor.getUserAccount().getUsername());
		Assert.notNull(actor.getUserAccount().getPassword());

		Assert.notNull(authority);

		boolean result = true;

		for (final Authority a : actor.getUserAccount().getAuthorities())
			if (!a.getAuthority().equals(authority)) {
				result = false;
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

	public Actor findByPrincipal() {
		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.actorRepository.count();

		return result;
	}

	public Actor findByName(final String name) {
		Assert.notNull(name);
		Actor result;

		result = this.actorRepository.findByName(name);

		return result;
	}

	public Collection<Actor> findAllSuspicious() {
		Collection<Actor> result;

		result = this.actorRepository.findAllSuspicious();

		return result;
	}

	// TODO It must be shown on a view when saving a phone.
	public boolean checkPhone(final String phone) {
		boolean result = false;

		final String PN = "^(\\d{4,})$";
		//		final String CC = "^[+]([1-9][0-9]{0,2})$";
		//		final String AC = "^[(]([1-9][0-9]{0,2})[)]$";

		final String CCPN = "^[+]([1-9][0-9]{0,2})(\\d{4,})$";
		final String CCACPN = "^[+]([1-9][0-9]{0,2})[(]([1-9][0-9]{0,2})[)](\\d{4,})$";

		if (phone.matches(PN) || phone.matches(CCPN) || phone.matches(CCACPN))
			result = true;

		return result;
	}

	public String updatePhone(final Actor actor) {
		final SystemConfiguration systemConfiguration = this.systemConfigurationService.findMain();
		final String CC = systemConfiguration.getDefaultCC();
		final String updatedPhone = CC.concat(actor.getPhone());

		return updatedPhone;
	}

	public String updatePhoneFromString(final String phone) {
		final SystemConfiguration systemConfiguration = this.systemConfigurationService.findMain();
		final String CC = systemConfiguration.getDefaultCC();
		final String updatedPhone = CC.concat(phone);

		return updatedPhone;
	}

	public boolean checkCreditCard(final CreditCard creditCard) {
		Assert.notNull(creditCard);

		boolean result = true;
		final int expirationMonth;
		final int expirationYear;

		expirationMonth = creditCard.getExpirationMonth();
		expirationYear = creditCard.getExpirationYear();

		if (expirationYear < Calendar.getInstance().get(Calendar.YEAR))
			result = false;
		else if ((expirationYear == Calendar.getInstance().get(Calendar.YEAR)) && expirationMonth < (Calendar.getInstance().get(Calendar.MONTH) + 1))
			result = false;

		return result;
	}

}
