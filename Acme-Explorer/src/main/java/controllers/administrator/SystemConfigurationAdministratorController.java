
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class SystemConfigurationAdministratorController extends AbstractController {

	// Services --------------------

	@Autowired
	private SystemConfigurationService	scService;


	// Constructor -----------------
	public SystemConfigurationAdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		SystemConfiguration systemConfiguration;
		systemConfiguration = this.scService.findMain();

		result = new ModelAndView("systemConfiguration/display");
		result.addObject("systemConfiguration", systemConfiguration);

		return result;
	}

	// Edition --------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		final SystemConfiguration systemConfiguration = this.scService.findMain();
		ModelAndView result;
		result = this.createEditModelAndView(systemConfiguration, null);

		return result;
	}

	// Save --------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid SystemConfiguration newSystemConfiguration, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(newSystemConfiguration);
		else
			try {
				newSystemConfiguration = this.scService.save(newSystemConfiguration);
				result = new ModelAndView("redirect:/systemConfiguration/administrator/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newSystemConfiguration, "systemConfiguration.commit.error");
			}
		return result;
	}

	// Ancillary methods -------------
	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration) {
		return this.createEditModelAndView(systemConfiguration, null);
	}

	protected ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration, final String message) {
		ModelAndView result;

		result = new ModelAndView("systemConfiguration/edit");
		result.addObject("systemConfiguration", systemConfiguration);
		result.addObject("message", message);

		return result;
	}
}
