
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.RendezvousService;
import domain.Actor;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;


	public RendezvousController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer rendezvousId, @RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses = new ArrayList<Rendezvous>();

		if (rendezvousId == null)
			try {
				final Actor a = this.actorService.findByPrincipal();
				rendezvouses = this.rendezvousService.findRendezvousesLogged(a);
			} catch (final Throwable oops) {
				rendezvouses = this.rendezvousService.findRendezvousesNotLogged();
			}
		else
			try {
				rendezvouses = this.rendezvousService.findRendezvousesSimilarsLogged(rendezvousId);
			} catch (final Throwable oops) {
				rendezvouses = this.rendezvousService.findRendezvousSimilarNotLogged(rendezvousId);
			}

		//RSVP button control
		Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();
		if (this.actorService.checkLogin()) {
			final Actor principal = this.actorService.findByPrincipal();
			principalRendezvouses = this.rendezvousService.findAllPrincipalRsvps(principal.getId());
		}

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("principalRendezvouses", principalRendezvouses);
		result.addObject("message", message);
		result.addObject("requestURI", "rendezvous/list.do");

		return result;
	}

}
