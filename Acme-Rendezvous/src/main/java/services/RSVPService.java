
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RSVPRepository;
import repositories.RendezvousRepository;
import domain.Answer;
import domain.Question;
import domain.RSVP;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class RSVPService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private RSVPRepository			rsvpRepository;

	@Autowired
	private RendezvousRepository	rendezvousService;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private AnswerService			answerService;


	// Constructors -----------------------------------------------------------

	public RSVPService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public RSVP create(final Rendezvous result) {

		final RSVP rsvp = new RSVP();
		final User u = this.userService.findByPrincipal();
		rsvp.setUser(u);

		rsvp.setIsCancelled(false);
		rsvp.setRendezvous(result);
		Assert.isTrue(!rsvp.getIsCancelled());

		return rsvp;
	}

	public Collection<RSVP> findAll() {
		Collection<RSVP> result = null;
		result = this.rsvpRepository.findAll();
		return result;
	}

	public RSVP findOne(final int rsvpId) {
		RSVP result = null;
		result = this.rsvpRepository.findOne(rsvpId);
		return result;
	}

	public RSVP save(final RSVP rsvp) {
		Assert.notNull(rsvp, "message.error.rsvp.null");
		final RSVP result = this.rsvpRepository.save(rsvp);

		return result;
	}

	public void delete(final RSVP rsvp) {
		Assert.notNull(rsvp, "message.error.rsvp.null");

	}

	public void cancelRSVP(final int rvId) {
		final Rendezvous rv = this.rendezvousService.findOne(rvId);
		Assert.notNull(rv, "message.error.rsvp.rendezvous.null");
		final User principal = this.userService.findByPrincipal();
		Assert.notNull(principal, "message.error.rsvp.principal.null");
		final RSVP rsvp = this.rsvpRepository.findRSVPByRendezvousAndUserId(rv.getId(), principal.getId());
		Assert.notNull(rsvp, "message.error.rsvp.null");

		final RSVP newRsvp = rsvp;
		newRsvp.setIsCancelled(true);
		principal.getRsvps().remove(rsvp);
		principal.getRsvps().add(newRsvp);

		this.save(newRsvp);
	}

	public void unCancelRSVP(final int rvId) {
		final Rendezvous rv = this.rendezvousService.findOne(rvId);
		Assert.notNull(rv, "message.error.rsvp.rendezvous.null");
		final User principal = this.userService.findByPrincipal();
		Assert.notNull(principal, "message.error.rsvp.principal.null");
		final RSVP rsvp = this.rsvpRepository.findRSVPByRendezvousAndUserId(rv.getId(), principal.getId());
		Assert.notNull(rsvp, "message.error.rsvp.null");

		final RSVP newRsvp = rsvp;
		newRsvp.setIsCancelled(false);
		principal.getRsvps().remove(rsvp);
		principal.getRsvps().add(newRsvp);

		this.save(newRsvp);
	}

	public void RSVPaRendezvous(final int rvId) {
		final Rendezvous rendezvousToRSVP = this.rendezvousService.findOne(rvId);
		final User creator = rendezvousToRSVP.getCreator();

		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(!(creator.equals(principal)), "message.error.rsvp.principal.creator.not");

		Collection<RSVP> principalRsvps = new ArrayList<RSVP>();

		principalRsvps = principal.getRsvps();

		final Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();

		for (final RSVP rsvp : principalRsvps)
			principalRendezvouses.add(rsvp.getRendezvous());

		Assert.isTrue(!(principalRendezvouses.contains(rendezvousToRSVP)), "message.error.RSVP.alreadyRSVPed.error");
		final Calendar currentCalendar = Calendar.getInstance();
		final Date currentDate = currentCalendar.getTime();
		Assert.isTrue(rendezvousToRSVP.getMeetingMoment().after(currentDate), "message.error.RSVP.past.error");

		final Date birthDate = principal.getBirthDate();
		final Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
		final Date yearLimit = now.getTime();

		if (rendezvousToRSVP.getIsAdultOnly())
			Assert.isTrue(birthDate.before(yearLimit), "message.error.RSVP.adult.error");

		Assert.isTrue(!rendezvousToRSVP.getIsDraft(), "message.error.RSVP.isDraft.error");
		final RSVP rsvp = this.create(rendezvousToRSVP);
		Assert.isTrue(!rsvp.getIsCancelled(), "message.error.RSVP.isCancelled.errors");
		rsvp.setRendezvous(rendezvousToRSVP);

		final RSVP result = this.save(rsvp);

		principal.getRsvps().add(result);
	}
	// Other business methods -------------------------------------------------
	public RSVP findRSVPByRendezvousAndUserId(final int rendezvousId, final int userId) {
		return this.rsvpRepository.findRSVPByRendezvousAndUserId(rendezvousId, userId);
	}

	public Collection<RSVP> findRSVPsCancelled(final int userId) {
		return this.rsvpRepository.findRSVPsCancelled(userId);
	}
	//Auxiliar methods

	//If return true, you can RSVP.
	@SuppressWarnings("unused")
	private Boolean allAnswersAreRespondedByPrincipal(final RSVP rsvp) {
		Boolean allAnswersAreRespondedByPrincipal;
		allAnswersAreRespondedByPrincipal = false;
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(rsvp.getRendezvous().getId());
		//An assert, be careful.
		Assert.notNull(rendezvousInDB);
		User userInDB;
		userInDB = this.userService.findOne(rsvp.getId());
		List<Question> questions;
		questions = new ArrayList<Question>();
		questions.addAll(rendezvousInDB.getQuestions());
		List<Answer> answersInDB;
		answersInDB = new ArrayList<Answer>();
		for (final Question q : questions) {
			Answer answer;
			answer = null;
			if (userInDB != null)
				answer = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), userInDB.getId());
			if (answer != null)
				answersInDB.add(answer);
		}
		if (questions.size() == answersInDB.size())
			allAnswersAreRespondedByPrincipal = true;
		return allAnswersAreRespondedByPrincipal;
	}

}
