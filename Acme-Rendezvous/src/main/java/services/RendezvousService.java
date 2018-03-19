
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RendezvousRepository;
import domain.Actor;
import domain.Announcement;
import domain.Comment;
import domain.Question;
import domain.RSVP;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Service
@Transactional
public class RendezvousService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private RendezvousRepository	rendezvousRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;
	@Autowired
	private RSVPService				rsvpService;


	// Constructors -----------------------------------------------------------

	public RendezvousService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Rendezvous create() {

		final Rendezvous result = new Rendezvous();
		final User u = this.userService.findByPrincipal();
		final Collection<RSVP> rsvps = new ArrayList<RSVP>();
		final Collection<Question> questions = new ArrayList<Question>();
		final Collection<Comment> comments = new ArrayList<Comment>();
		final Collection<Announcement> announcements = new ArrayList<Announcement>();
		final Collection<Rendezvous> isLinkedTo = new ArrayList<Rendezvous>();
		final Collection<Request> requests = new ArrayList<Request>();

		result.setIsDeleted(false);
		result.setCreator(u);
		result.setRsvps(rsvps);
		result.setQuestions(questions);
		result.setComments(comments);
		result.setAnnouncements(announcements);
		result.setIsLinkedTo(isLinkedTo);
		result.setRequests(requests);

		return result;
	}

	public void flush() {
		this.rendezvousRepository.flush();
	}

	public Collection<Rendezvous> findRendezvousByCategories(final int categoryId) {
		return this.rendezvousRepository.findRendezvousByCategories(categoryId);
	}

	public Collection<Rendezvous> findRendezvousSimilarNotDeleted() {
		final User u = this.userService.findByPrincipal();
		return this.rendezvousRepository.findRendezvousSimilarNotDeleted(u.getId());

	}
	public Rendezvous findRendezvousByService(final int serviceId) {
		return this.rendezvousRepository.findRendezvousByService(serviceId);
	}

	public Collection<Rendezvous> findRendezvousSimilar(final int rendezvousId) {
		return this.rendezvousRepository.findRendezvousSimilar(rendezvousId);
	}

	public Collection<Rendezvous> findAll() {
		Collection<Rendezvous> result = null;
		result = this.rendezvousRepository.findAll();
		return result;
	}

	public Rendezvous findOne(final int rendezvousId) {
		Rendezvous result = null;
		result = this.rendezvousRepository.findOne(rendezvousId);
		return result;
	}

	public Collection<Rendezvous> findRendezvousesLogged(final Actor a) {

		final Calendar birthDate = new GregorianCalendar();
		birthDate.setTime(a.getBirthDate());
		final int age = this.calculateAge(birthDate);
		if (age < 18)
			return this.findRendezvousesAllUser();
		else
			return this.findRendezvousesOnlyAdult();

	}

	public Collection<Rendezvous> findRendezvousesSimilarsLogged(final int rendezvousId) {

		final Actor a = this.actorService.findByPrincipal();
		final Calendar birthDate = new GregorianCalendar();
		birthDate.setTime(a.getBirthDate());
		final int age = this.calculateAge(birthDate);
		if (age < 18)
			return this.findRendezvousSimilarNotLogged(rendezvousId);
		else
			return this.findRendezvousSimilarLogged(rendezvousId);

	}

	public Collection<Rendezvous> findRendezvousSimilarLogged(final int rendezvousId) {
		return this.rendezvousRepository.findRendezvousSimilarLogged(rendezvousId);
	}

	public Collection<Rendezvous> findRendezvousSimilarNotLogged(final int rendezvousId) {
		return this.rendezvousRepository.findRendezvousSimilarNotLogged(rendezvousId);
	}

	public Collection<Rendezvous> findRendezvousesNotLogged() {
		return this.findRendezvousesAllUser();
	}

	public Collection<Rendezvous> findRendezvousesAllUser() {
		return this.rendezvousRepository.findRendezvousesAllUser();
	}

	public Collection<Rendezvous> findRendezvousesOnlyAdult() {
		return this.rendezvousRepository.findRendezvousesOnlyAdult();
	}

	public Rendezvous save(final Rendezvous rendezvous) {
		Assert.notNull(rendezvous, "message.error.rendezvous.null");
		Assert.isTrue(rendezvous.getMeetingMoment().after(new Date()), "message.error.rendezvous.meetingMoment.future");
		Assert.notNull(rendezvous.getGpsPoint());
		Assert.isTrue(!((rendezvous.getGpsPoint().getLongitude() == null && rendezvous.getGpsPoint().getLatitude() != null) || (rendezvous.getGpsPoint().getLongitude() != null && rendezvous.getGpsPoint().getLatitude() == null)),
			"message.error.rendezvous.GPSPoint");

		final Calendar birthDate = new GregorianCalendar();
		int age = 0;
		if (rendezvous.getCreator().getBirthDate() != null) {
			birthDate.setTime(rendezvous.getCreator().getBirthDate());
			age = this.calculateAge(birthDate);
		}
		if (age < 18)
			Assert.isTrue(rendezvous.getIsAdultOnly() == false);

		Rendezvous result;
		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	public Rendezvous saveWithoutConstraints(final Rendezvous rendezvous) {
		Assert.notNull(rendezvous, "message.error.rendezvous.null");

		Rendezvous result;

		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	public Rendezvous update(final Rendezvous rendezvous) {

		Assert.notNull(rendezvous, "message.error.rendezvous.null");
		final User u = this.userService.findByPrincipal();
		Assert.isTrue(rendezvous.getCreator().equals(u), "message.error.rendezvous.user");
		Assert.isTrue(!rendezvous.getIsDeleted(), "message.error.rendezvous.isDeleted");

		Assert.isTrue(rendezvous.getMeetingMoment().after(new Date()), "message.error.rendezvous.meetingMoment.future");
		Assert.isTrue(!((rendezvous.getGpsPoint().getLongitude() == null && rendezvous.getGpsPoint().getLatitude() != null) || (rendezvous.getGpsPoint().getLongitude() != null && rendezvous.getGpsPoint().getLatitude() == null)),
			"message.error.rendezvous.GPSPoint");

		final Calendar birthDate = new GregorianCalendar();
		int age = 0;
		if (rendezvous.getCreator().getBirthDate() != null) {
			birthDate.setTime(rendezvous.getCreator().getBirthDate());
			age = this.calculateAge(birthDate);
		}
		if (age < 18)
			Assert.isTrue(rendezvous.getIsAdultOnly() == false);

		Rendezvous result;
		result = this.rendezvousRepository.save(rendezvous);

		return result;
	}

	public void delete(final int rendezvousId) {
		final Rendezvous r = this.rendezvousRepository.findOne(rendezvousId);
		final User u = this.userService.findByPrincipal();
		Assert.notNull(r, "message.error.rendezvous.null");
		Assert.isTrue(r.getCreator().equals(u), "message.error.rendezvous.user");
		Assert.isTrue(r.getIsDraft(), "message.error.rendezvous.isDraft");
		Assert.isTrue(!r.getIsDeleted(), "message.error.rendezvous.isDeleted");
		r.setIsDeleted(true);
		this.rendezvousRepository.save(r);

	}

	public void deleteAdmin(final int rendezvousId) {
		final Rendezvous r = this.rendezvousRepository.findOne(rendezvousId);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(actor, "ADMIN"));
		Assert.notNull(r, "message.error.rendezvous.null");
		final User creator = r.getCreator();
		creator.getRendezvoussesCreated().remove(r);
		this.userService.save(creator);
		final Collection<RSVP> rsvps = r.getRsvps();
		for (final RSVP rv : rsvps) {
			final User u = rv.getUser();
			u.getRsvps().remove(rv);
			this.userService.save(u);
			this.rsvpService.delete(rv);
		}
		final Collection<Rendezvous> cr = r.getIsLinkedTo();
		for (final Rendezvous r2 : cr) {
			r2.getIsLinkedTo().remove(r);
			this.rendezvousRepository.save(r2);
		}

		this.rendezvousRepository.delete(r);

	}

	public void linked(final Rendezvous r1, final Rendezvous r2) {

		final User u = this.userService.findByPrincipal();
		Assert.notNull(r1, "message.error.rendezvous.null");
		Assert.notNull(r2, "message.error.rendezvous.null");
		Assert.isTrue(r1.getCreator().equals(u), "message.error.rendezvous.user");
		Assert.isTrue(r2.getCreator().equals(u), "message.error.rendezvous.user");
		Assert.isTrue(!r1.getIsLinkedTo().contains(r2));
		Assert.isTrue(!r2.getIsLinkedTo().contains(r1));

		r1.getIsLinkedTo().add(r2);
		r2.getIsLinkedTo().add(r1);
		this.rendezvousRepository.save(r1);
		this.rendezvousRepository.save(r2);
	}

	// Other business methods -------------------------------------------------

	public Collection<Rendezvous> findAllAttendedByUserId(final int userId) {
		final Collection<Rendezvous> result;
		result = this.rendezvousRepository.findAllAttendedByUserId(userId);
		return result;
	}

	public Collection<Rendezvous> findAllAttendedByUserIdU18(final int userId) {
		Collection<Rendezvous> result;
		result = this.rendezvousRepository.findAllAttendedByUserIdU18(userId);
		return result;
	}

	public Collection<Rendezvous> findAllAvailableRendezvousesCreatedByUserId(final int userId) {
		Collection<Rendezvous> result = null;
		result = this.rendezvousRepository.findAllAvailableRendezvousesCreatedByUserId(userId);
		return result;
	}

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 1.0 - Requisito 6.3.1
	public Double findAvgRendezvousesCreatedPerUser() {
		Double result = null;
		result = this.rendezvousRepository.findAvgRendezvousesCreatedPerUser();
		return result;
	}

	public Double findStdRendezvousesCreatedPerUser() {
		Double result = null;
		result = this.rendezvousRepository.findStdRendezvousesCreatedPerUser();
		return result;
	}

	// Acme-Rendezvous 1.0 - Requisito 6.3.4
	public Double findAvgRendezvousRSVPsPerUsers() {
		Double result = null;
		result = this.rendezvousRepository.findAvgRendezvousRSVPsPerUsers();
		return result;
	}

	public Double findStdRendezvousRSVPsPerUsers() {
		Double result = null;
		result = this.rendezvousRepository.findStdRendezvousRSVPsPerUsers();
		return result;
	}

	// Acme-Rendezvous 1.0 - Requisito 6.3.5
	public Collection<Rendezvous> findTop10RendezvousByRSVPs() {
		Collection<Rendezvous> result = null;
		result = this.rendezvousRepository.findAllRendezvousByRSVPs();

		if (result != null && result.size() > 10)
			result = new ArrayList<Rendezvous>(result).subList(0, 10);

		return result;
	}

	// Acme-Rendezvous 1.0 - Requisito 17.2.2
	public Collection<Rendezvous> findAllRendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous() {
		Collection<Rendezvous> result = null;
		result = this.rendezvousRepository.findAllRendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous();
		return result;
	}

	// Acme-Rendezvous 1.0 - Requisito 17.2.3
	public Collection<Rendezvous> findRendezvousesThatLinkedToRvGreaterThanAvgPlus10() {
		Collection<Rendezvous> result = null;
		result = this.rendezvousRepository.findRendezvousesThatLinkedToRvGreaterThanAvgPlus10();
		return result;
	}

	public int calculateAge(final Calendar fechaNac) {
		final Calendar today = Calendar.getInstance();

		int year = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
		final int month = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
		final int day = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

		//Si aun no los ha cumplido
		if (month < 0 || (month == 0 && day < 0))
			year = year - 1;
		return year;
	}
	public Collection<Rendezvous> findAllPrincipalRsvps(final int userId) {
		return this.rendezvousRepository.findAllPrincipalRsvps(userId);
	}
}
