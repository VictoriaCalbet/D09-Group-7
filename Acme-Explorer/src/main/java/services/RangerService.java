
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RangerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Folder;
import domain.Message;
import domain.Ranger;
import domain.SocialIdentity;
import domain.Trip;

@Service
@Transactional
public class RangerService {

	// Managed Repository
	@Autowired
	private RangerRepository		rangerRepository;

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

	public RangerService() {
		super();
	}

	// Simple CRUD methods
	public Ranger create() {
		Ranger result;
		UserAccount userAccount;

		result = new Ranger();
		result.setIsSuspicious(false);
		result.setIsBanned(false);
		result.setTrips(new HashSet<Trip>());

		userAccount = this.userAccountService.create("RANGER");
		userAccount.setIsEnabled(true);
		result.setUserAccount(userAccount);
		result.setSocialIdentities(new HashSet<SocialIdentity>());
		result.setSent(new HashSet<Message>());
		result.setReceived(new HashSet<Message>());
		result.setFolders(new HashSet<Folder>());

		return result;
	}

	private Ranger save(final Ranger ranger) {
		Assert.notNull(ranger);

		Ranger result;

		result = this.rangerRepository.save(ranger);

		return result;
	}

	public Ranger saveFromCreate(final Ranger ranger) {
		Assert.notNull(ranger, "message.error.ranger.null");
		Assert.notNull(ranger.getUserAccount().getUsername(), "message.error.ranger.username");
		Assert.notNull(ranger.getUserAccount().getPassword(), "message.error.ranger.password");

		Ranger result;
		Collection<Folder> newFolders;

		// Check RANGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkRanger(ranger);
		Assert.isTrue(correctAuthority, "message.error.ranger.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(ranger.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.ranger.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = ranger.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		ranger.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(ranger);
		ranger.setFolders(newFolders);

		// Add CC if PN phone
		if (ranger.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(ranger);
			ranger.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = ranger.getIsSuspicious() || this.messageService.checkSpam(ranger.getName()) || this.messageService.checkSpam(ranger.getSurname()) || this.messageService.checkSpam(ranger.getEmail());

		if (ranger.getPhone() != null && ranger.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getPhone());

		if (ranger.getAddress() != null && ranger.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getAddress());

		// Set suspicious to false
		ranger.setIsSuspicious(isSuspicious);
		ranger.setIsBanned(false);

		// Save ranger
		result = this.save(ranger);

		return result;
	}

	public Ranger saveFromCreateByAdmin(final Ranger ranger) {
		Assert.notNull(ranger, "message.error.ranger.null");
		Assert.notNull(ranger.getUserAccount().getUsername(), "message.error.ranger.username");
		Assert.notNull(ranger.getUserAccount().getPassword(), "message.error.ranger.password");

		Ranger result;
		Collection<Folder> newFolders;

		// Check RANGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkRanger(ranger);
		Assert.isTrue(correctAuthority, "message.error.ranger.authority");

		// Check username
		UserAccount possibleRepeated = null;
		possibleRepeated = this.userAccountService.findByUserNameWithoutAsserts(ranger.getUserAccount().getUsername());
		Assert.isNull(possibleRepeated, "message.error.ranger.username.repeated");

		// Encoding password.
		UserAccount userAccount;
		userAccount = ranger.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		ranger.setUserAccount(userAccount);

		// Initialise folders.
		newFolders = this.folderService.initializeActor(ranger);
		ranger.setFolders(newFolders);

		// Add CC if PN phone
		if (ranger.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(ranger);
			ranger.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		final Administrator administrator = this.administratorService.findByPrincipal();
		boolean isSuspicious = administrator.getIsSuspicious() || this.messageService.checkSpam(ranger.getName()) || this.messageService.checkSpam(ranger.getSurname()) || this.messageService.checkSpam(ranger.getEmail());

		if (ranger.getPhone() != null && ranger.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getPhone());

		if (ranger.getAddress() != null && ranger.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getAddress());

		administrator.setIsSuspicious(isSuspicious);
		this.administratorService.saveFromEdit(administrator);

		// Set suspicious to false
		ranger.setIsSuspicious(false);
		ranger.setIsBanned(false);

		// Save ranger
		result = this.save(ranger);

		return result;
	}

	public Ranger saveFromEditWithEncoding(final Ranger ranger) {
		Assert.notNull(ranger, "message.error.ranger.null");
		Assert.notNull(ranger.getUserAccount().getUsername(), "message.error.ranger.username");
		Assert.notNull(ranger.getUserAccount().getPassword(), "message.error.ranger.password");

		Ranger result;
		Ranger principal;

		principal = this.findByPrincipal();
		Assert.notNull(principal, "message.error.ranger.login");
		Assert.isTrue(principal.getId() == ranger.getId(), "message.error.ranger.principal");

		// Check RANGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkRanger(ranger);
		Assert.isTrue(correctAuthority, "message.error.ranger.authority");

		// Encoding password.
		UserAccount userAccount;
		userAccount = ranger.getUserAccount();
		userAccount = this.userAccountService.encodePassword(userAccount);
		ranger.setUserAccount(userAccount);

		// Add CC if PN phone
		if (ranger.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(ranger);
			ranger.setPhone(updatedPhone);
		}

		// Set suspicious of the creator
		boolean isSuspicious = ranger.getIsSuspicious() || this.messageService.checkSpam(ranger.getName()) || this.messageService.checkSpam(ranger.getSurname()) || this.messageService.checkSpam(ranger.getEmail());

		if (ranger.getPhone() != null && ranger.getPhone() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getPhone());

		if (ranger.getAddress() != null && ranger.getAddress() != "")
			isSuspicious = isSuspicious || this.messageService.checkSpam(ranger.getAddress());

		ranger.setIsSuspicious(isSuspicious);

		// Save ranger
		result = this.save(ranger);

		return result;
	}

	public Ranger saveFromEdit(final Ranger ranger) {
		Assert.notNull(ranger, "message.error.ranger.null");
		Assert.notNull(ranger.getUserAccount().getUsername(), "message.error.ranger.username");
		Assert.notNull(ranger.getUserAccount().getPassword(), "message.error.ranger.password");

		Ranger result;

		// Check RANGER authoriry.
		final boolean correctAuthority;
		correctAuthority = this.checkRanger(ranger);
		Assert.isTrue(correctAuthority, "message.error.ranger.authority");

		// Add CC if PN phone
		if (ranger.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhone(ranger);
			ranger.setPhone(updatedPhone);
		}

		// Save ranger
		result = this.save(ranger);

		return result;
	}

	public Ranger ban(final int rangerId) {
		Ranger result;
		Ranger rangerToBan;
		UserAccount userAccountToBan;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.ranger.ban.admin");

		rangerToBan = this.rangerRepository.findOne(rangerId);
		Assert.notNull(rangerToBan, "message.error.ranger.null");
		Assert.isTrue(rangerToBan.getIsSuspicious(), "message.error.ranger.ban.suspicious");
		rangerToBan.setIsBanned(true);

		// De-activate the user account.
		userAccountToBan = rangerToBan.getUserAccount();
		userAccountToBan.setIsEnabled(false);
		rangerToBan.setUserAccount(userAccountToBan);

		result = this.save(rangerToBan);

		return result;
	}

	public Ranger unban(final int rangerId) {
		Ranger result;
		Ranger rangerToUnban;
		final UserAccount userAccountToUnban;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN), "message.error.ranger.ban.admin");

		rangerToUnban = this.rangerRepository.findOne(rangerId);
		Assert.notNull(rangerToUnban, "message.error.ranger.null");
		Assert.isTrue(rangerToUnban.getIsBanned(), "message.error.ranger.ban.banned");
		rangerToUnban.setIsBanned(false);

		// Re-activate the user account.
		userAccountToUnban = rangerToUnban.getUserAccount();
		userAccountToUnban.setIsEnabled(true);
		rangerToUnban.setUserAccount(userAccountToUnban);

		result = this.save(rangerToUnban);

		return result;
	}

	// Other business methods
	public Collection<Ranger> findAll() {
		Collection<Ranger> result;

		result = this.rangerRepository.findAll();

		return result;
	}

	public Ranger findOne(final int rangerId) {
		Ranger result;

		result = this.rangerRepository.findOne(rangerId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.rangerRepository.count();

		return result;
	}

	public Collection<Ranger> findAllSuspicious() {
		Collection<Ranger> result;

		result = this.rangerRepository.findAllSuspicious();

		return result;
	}

	public Collection<Ranger> findAllNotBanned() {
		Collection<Ranger> result;

		result = this.rangerRepository.findAllNotBanned();

		return result;
	}

	//Devuelve el manager que esta creado
	public Ranger findByPrincipal() {
		Ranger result;
		UserAccount userAccount;
		//Coge la cuenta de usuario que esta logueado ahora mismo
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.rangerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}
	//B-30 Find a Ranger by a trip id
	public Ranger findByTripId(final int tripId) {

		Ranger result;

		result = this.rangerRepository.findByTripId(tripId);

		return result;

	}

	private boolean checkRanger(final Ranger ranger) {
		Assert.notNull(ranger);
		Assert.notNull(ranger.getUserAccount().getUsername());
		Assert.notNull(ranger.getUserAccount().getPassword());

		boolean result = true;

		result = this.actorService.checkActorWithAuthority(ranger, Authority.RANGER);

		return result;
	}

	//C-14.6.4.1 The average number trips guided per ranger
	public Double findAverageTripPerRanger() {
		return this.rangerRepository.findAverageTripPerRanger();
	}
	//C-14.6.4.2 The minimum number trips guided per ranger
	public Integer findMinTripsPerRanger() {
		return this.rangerRepository.findMinTripsPerRanger();
	}
	//C-14.6.4.3 The maximum number trips guided per ranger
	public Integer findMaxTripsPerRanger() {
		return this.rangerRepository.findMaxTripsPerRanger();
	}
	//C-14.6.4.4 The standard deviation of the number trips guided per ranger
	public Double findStandardDeviationOfTripsPerRanger() {
		return this.rangerRepository.findStandardDeviationOfTripsPerRanger();
	}

	//B-16.4.4 The ratio of rangers who have registered their curricula
	public Double findRatioOfRangersWithCurricula() {
		return this.rangerRepository.findRatioOfRangersWithCurricula();
	}
	//B-16.4.5 The ratio of rangers whose curriculum’s been endorsed.
	public Double findRatioOfRangersWithCurriculumEndorsed() {
		return this.rangerRepository.findRatioOfRangersWithCurriculumEndorsed();
	}
	//B-16.4.7 The ratio of suspicious rangers
	public Double findRatioOfSuspiciousRangers() {
		return this.rangerRepository.findRatioOfSuspiciousRangers();
	}
}
