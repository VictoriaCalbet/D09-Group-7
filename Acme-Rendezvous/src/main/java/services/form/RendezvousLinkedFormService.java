
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import services.RendezvousService;
import services.UserService;
import domain.Rendezvous;
import domain.User;
import domain.form.RendezvousLinkedForm;

@Service
@Transactional
public class RendezvousLinkedFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public RendezvousLinkedFormService() {
		super();
	}

	public RendezvousLinkedForm create(final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final User u = this.userService.findByPrincipal();
		Assert.notNull(r, "message.error.rendezvous.null");
		Assert.isTrue(r.getCreator().equals(u), "message.error.rendezvous.user");
		final RendezvousLinkedForm lk = new RendezvousLinkedForm();
		lk.setRendezvousId(rendezvousId);

		return lk;

	}

	public void linkedTo(final RendezvousLinkedForm r) {

		Assert.notNull(r, "message.error.rendezvous.null");

		final Rendezvous r1 = this.rendezvousService.findOne(r.getRendezvousId());
		final Rendezvous r2 = this.rendezvousService.findOne(r.getRendezvousLinkedId());

		this.rendezvousService.linked(r1, r2);
	}

}
