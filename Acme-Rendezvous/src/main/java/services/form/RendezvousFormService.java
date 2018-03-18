
package services.form;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import services.RSVPService;
import services.RendezvousService;
import services.UserService;
import domain.RSVP;
import domain.Rendezvous;
import domain.User;
import domain.form.RendezvousForm;

@Service
@Transactional
public class RendezvousFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private RSVPService			rsvpService;
	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public RendezvousFormService() {
		super();
	}

	public RendezvousForm create() {
		RendezvousForm result;

		result = new RendezvousForm();

		return result;
	}

	public RendezvousForm create(final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final User u = this.userService.findByPrincipal();

		Assert.isTrue(r.getCreator().equals(u), "message.error.rendezvous.user");
		Assert.isTrue(r.getIsDraft(), "message.error.rendezvous.isDraft");
		Assert.isTrue(!r.getIsDeleted(), "message.error.rendezvous.isDeleted");

		final RendezvousForm rF = new RendezvousForm();
		rF.setDescription(r.getDescription());
		rF.setName(r.getName());
		rF.setGpsPoint(r.getGpsPoint());
		rF.setIsAdultOnly(r.getIsAdultOnly());
		rF.setIsDraft(r.getIsDraft());
		rF.setMeetingMoment(r.getMeetingMoment());
		rF.setPicture(r.getPicture());
		rF.setId(r.getId());

		return rF;
	}

	public Rendezvous saveFromCreate(final RendezvousForm rendezvousForm) {

		final Rendezvous r = this.rendezvousService.create();

		r.setDescription(rendezvousForm.getDescription());
		r.setName(rendezvousForm.getName());
		r.setGpsPoint(rendezvousForm.getGpsPoint());
		r.setIsAdultOnly(rendezvousForm.getIsAdultOnly());
		r.setIsDraft(rendezvousForm.getIsDraft());
		r.setMeetingMoment(rendezvousForm.getMeetingMoment());
		r.setPicture(rendezvousForm.getPicture());

		final Rendezvous rdv = this.rendezvousService.save(r);
		final Collection<RSVP> rsvps = new ArrayList<RSVP>();
		final RSVP rsvp = this.rsvpService.create(rdv);
		final RSVP rsvpSave = this.rsvpService.save(rsvp);
		rsvps.add(rsvpSave);
		rdv.setRsvps(rsvps);

		final User u = this.userService.findByPrincipal();
		final Collection<Rendezvous> rendezvouses = u.getRendezvoussesCreated();
		rendezvouses.add(rdv);

		return this.rendezvousService.save(rdv);

	}

	public Rendezvous saveFromEdit(final RendezvousForm rendezvousForm) {

		final Rendezvous r = this.rendezvousService.findOne(rendezvousForm.getId());
		Assert.isTrue(r.getIsDraft(), "message.error.rendezvous.isDraft");
		r.setDescription(rendezvousForm.getDescription());
		r.setName(rendezvousForm.getName());
		r.setGpsPoint(rendezvousForm.getGpsPoint());
		r.setIsAdultOnly(rendezvousForm.getIsAdultOnly());
		r.setIsDraft(rendezvousForm.getIsDraft());
		r.setMeetingMoment(rendezvousForm.getMeetingMoment());
		r.setPicture(rendezvousForm.getPicture());

		final Rendezvous rdv = this.rendezvousService.update(r);

		return rdv;
	}
}
