
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.RangerService;
import controllers.AbstractController;
import domain.Ranger;

@Controller
@RequestMapping("/ranger/administrator")
public class RangerAdministratorController extends AbstractController {

	// Services
	@Autowired
	private RangerService	rangerService;


	// Constructor
	public RangerAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Ranger> rangers;

		rangers = this.rangerService.findAll();

		result = new ModelAndView("ranger/list");
		result.addObject("rangers", rangers);
		result.addObject("requestURI", "ranger/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/listSuspicious", method = RequestMethod.GET)
	public ModelAndView listSuspicious() {
		ModelAndView result;
		Collection<Ranger> rangers;

		rangers = this.rangerService.findAllSuspicious();

		result = new ModelAndView("ranger/list");
		result.addObject("rangers", rangers);
		result.addObject("requestURI", "ranger/administrator/listSuspicious.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Ranger ranger;

		ranger = this.rangerService.create();
		result = this.createEditModelAndView(ranger);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Ranger ranger, final BindingResult binding) {
		ModelAndView result;
		boolean bindingError;

		if (binding.hasFieldErrors("folders"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;

		if (bindingError)
			result = this.createEditModelAndView(ranger);
		else
			try {
				this.rangerService.saveFromCreateByAdmin(ranger);
				result = new ModelAndView("redirect:../..");
			} catch (final Throwable oops) {
				String messageError = "ranger.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(ranger, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(final int rangerId) {
		final ModelAndView result = new ModelAndView("ranger/list");
		Collection<Ranger> rangers;

		try {
			this.rangerService.ban(rangerId);
		} catch (final Throwable oops) {
			String messageError = "ranger.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		rangers = this.rangerService.findAllSuspicious();
		result.addObject("rangers", rangers);
		result.addObject("requestURI", "ranger/administrator/listSuspicious.do");

		return result;

	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(final int rangerId) {
		final ModelAndView result = new ModelAndView("ranger/list");
		Collection<Ranger> rangers;

		try {
			this.rangerService.unban(rangerId);
		} catch (final Throwable oops) {
			String messageError = "ranger.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		rangers = this.rangerService.findAllSuspicious();
		result.addObject("rangers", rangers);
		result.addObject("requestURI", "ranger/administrator/listSuspicious.do");

		return result;

	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Ranger ranger) {
		ModelAndView result;

		result = this.createEditModelAndView(ranger, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Ranger ranger, final String message) {
		ModelAndView result;

		result = new ModelAndView("ranger/edit");
		result.addObject("ranger", ranger);
		result.addObject("message", message);
		result.addObject("requestURI", "ranger/administrator/edit.do");

		return result;
	}
}
