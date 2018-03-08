
package controllers.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/actor/actor")
public class ActorActorController extends AbstractController {

	@Autowired
	private ActorService	actorService;


	public ActorActorController() {
		super();
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile() {
		ModelAndView result = new ModelAndView();

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		result = this.infoModelAndView(actor);
		return result;
	}

	protected ModelAndView infoModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.infoModelAndView(actor, null);

		return result;
	}

	protected ModelAndView infoModelAndView(final Actor actor, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/info");
		result.addObject("user", actor);
		result.addObject("message", message);
		result.addObject("requestURI", "actor/actor/profile.do");

		return result;
	}

}
