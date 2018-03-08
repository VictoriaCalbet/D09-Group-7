
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ExplorerService;
import domain.Explorer;

@Controller
@RequestMapping("/explorer")
public class ExplorerController extends AbstractController {

	// Services
	@Autowired
	private ExplorerService	explorerService;


	// Constructor
	public ExplorerController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Explorer explorer;

		explorer = this.explorerService.create();
		result = this.createEditModelAndView(explorer);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Explorer explorer, final BindingResult binding) {
		ModelAndView result;

		boolean bindingError;
		if (binding.hasFieldErrors("folders"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;
		if (bindingError)
			result = this.createEditModelAndView(explorer);
		else
			try {
				this.explorerService.saveFromCreate(explorer);
				result = new ModelAndView("redirect:../");
			} catch (final Throwable oops) {
				String messageError = "explorer.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(explorer, messageError);
			}
		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Explorer explorer) {
		ModelAndView result;

		result = this.createEditModelAndView(explorer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Explorer explorer, final String message) {
		ModelAndView result;

		result = new ModelAndView("explorer/edit");
		result.addObject("explorer", explorer);
		result.addObject("message", message);
		result.addObject("requestURI", "explorer/edit.do");

		return result;
	}
}
