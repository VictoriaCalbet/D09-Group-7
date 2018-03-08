
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ActorService	actorService;


	// Constructor
	public ActorAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Actor> actors;

		actors = this.actorService.findAllSuspicious();

		result = new ModelAndView("actor/list");
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(final int actorId) {
		final ModelAndView result = new ModelAndView("actor/list");
		Collection<Actor> actors;

		try {
			this.actorService.ban(actorId);
		} catch (final Throwable oops) {
			String messageError = "manager.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		actors = this.actorService.findAllSuspicious();
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/list.do");

		return result;

	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(final int actorId) {
		final ModelAndView result = new ModelAndView("actor/list");
		Collection<Actor> actors;

		try {
			this.actorService.unban(actorId);
		} catch (final Throwable oops) {
			String messageError = "manager.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		actors = this.actorService.findAllSuspicious();
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/list.do");

		return result;

	}
}
