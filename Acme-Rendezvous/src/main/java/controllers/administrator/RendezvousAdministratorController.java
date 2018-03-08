
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import controllers.AbstractController;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous/administrator")
public class RendezvousAdministratorController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;


	public RendezvousAdministratorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses = new ArrayList<Rendezvous>();
		rendezvouses = this.rendezvousService.findAll();

		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("message", message);
		result.addObject("requestURI", "rendezvous/administrator/list.do");

		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int rendezvousId) {
		ModelAndView result;
		try {
			this.rendezvousService.deleteAdmin(rendezvousId);
			result = new ModelAndView("redirect:/rendezvous/administrator/list.do");
		} catch (final Throwable oops) {
			String messageError = "rendezvous.delete.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/rendezvous/administrator/list.do");
			result.addObject("message", messageError);
		}
		return result;
	}

}
