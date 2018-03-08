
package controllers.explorer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ExplorerService;
import controllers.AbstractController;
import domain.Explorer;

@Controller
@RequestMapping("/explorer/explorer")
public class ExplorerExplorerController extends AbstractController {

	// Services
	@Autowired
	private ExplorerService	explorerService;


	// Constructor
	public ExplorerExplorerController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Explorer explorer;

		explorer = this.explorerService.findByPrincipal();
		Assert.notNull(explorer);
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
				this.explorerService.saveFromEditWithEncoding(explorer);
				result = new ModelAndView("redirect:../..");
			} catch (final Throwable oops) {
				String messageError = "manager.commit.error";
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
		result.addObject("requestURI", "explorer/explorer/edit.do");

		return result;
	}
}
