
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Answer;
import domain.Comment;
import domain.RSVP;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class UserService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private UserRepository		userRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------------

	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		User result = null;
		UserAccount userAccount = null;

		result = new User();
		userAccount = this.userAccountService.create("USER");
		result.setAnswers(new HashSet<Answer>());
		result.setComments(new HashSet<Comment>());
		result.setRsvps(new HashSet<RSVP>());
		result.setRendezvoussesCreated(new HashSet<Rendezvous>());
		result.setUserAccount(userAccount);

		return result;
	}

	public User save(final User user) {
		Assert.notNull(user, "message.error.user.null");
		User result;

		result = this.userRepository.save(user);

		return result;
	}

	public User saveFromCreate(final User user) {
		Assert.notNull(user, "message.error.user.null");
		Assert.notNull(user.getUserAccount().getUsername(), "message.error.user.username.null");
		Assert.notNull(user.getUserAccount().getPassword(), "message.error.user.password.null");

		if (user.getBirthDate() != null)
			Assert.isTrue(user.getBirthDate().before(new Date()), "message.error.user.birthDate.past");

		final User result;

		// Check unlogged user
		Assert.isTrue(!this.actorService.checkLogin(), "message.error.user.login");

		// Check authority
		final boolean isUser;
		isUser = this.actorService.checkAuthority(user, "USER");
		Assert.isTrue(isUser, "message.error.user.authority.wrong");

		// Check repeated username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUsername(user.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.user.username.repeated");

		result = this.save(user);

		return result;
	}

	public User saveFromEdit(final User user) {
		Assert.notNull(user, "message.error.user.null");
		Assert.notNull(user.getUserAccount().getUsername(), "message.error.user.username.null");
		Assert.notNull(user.getUserAccount().getPassword(), "message.error.user.password.null");

		if (user.getBirthDate() != null)
			Assert.isTrue(user.getBirthDate().before(new Date()), "message.error.user.birthDate.past");

		final User result;
		User principal;

		// Check user as principal
		principal = this.findByPrincipal();
		Assert.isTrue(principal.getId() == user.getId(), "message.error.user.edit.self");

		// Check authority
		final boolean isUser;
		isUser = this.actorService.checkAuthority(user, "USER");
		Assert.isTrue(isUser, "message.error.user.authority.wrong");

		// Encoding password
		UserAccount userAccount;
		userAccount = user.getUserAccount();
		userAccount = this.userAccountService.modifyPassword(userAccount);
		user.setUserAccount(userAccount);

		result = this.save(user);

		return result;
	}

	// Other business methods -------------------------------------------------

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.userRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result = null;
		result = this.userRepository.findAll();
		return result;
	}

	public User findOne(final int userId) {
		User result = null;
		result = this.userRepository.findOne(userId);
		return result;
	}

	public Collection<User> findAttendantsOfRendezvous(final int rendezvousId) {
		return this.userRepository.findAttendantsOfRendezvous(rendezvousId);
	}

	// Dashboard methods ------------------------------------------------------

	public Double findRatioUserRendezvousesCreatedVsNeverCreated() {
		Double result = null;
		result = this.userRepository.findRatioUserRendezvousesCreatedVsNeverCreated();
		return result;
	}

	public Double findAvgUsersRSVPsPerRendezvous() {
		Double result = null;
		result = this.userRepository.findAvgUsersRSVPsPerRendezvous();
		return result;
	}

	public Double findStdUsersRSVPsPerRendezvous() {
		Double result = null;
		result = this.userRepository.findStdUsersRSVPsPerRendezvous();
		return result;
	}
}
