
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

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Constructors -----------------------------------------------------------

	public SystemConfigurationAdministratorController() {
		super();
	}

	// Info -------------------------------------------------------------------

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ModelAndView info() {
		ModelAndView result;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.systemConfigurationService.findMain();

		result = new ModelAndView("systemConfiguration/info");
		result.addObject("systemConfiguration", systemConfiguration);
		result.addObject("requestURI", "systemConfiguration/administrator/info.do");

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.systemConfigurationService.findMain();
		result = this.createEditModelAndView(systemConfiguration);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SystemConfiguration systemConfiguration, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(systemConfiguration);
		else
			try {
				this.systemConfigurationService.saveFromEdit(systemConfiguration);
				result = new ModelAndView("redirect:info.do");
			} catch (final Throwable oops) {
				String messageError = "systemConfiguration.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(systemConfiguration, messageError);
			}

		return result;
	}

	// Ancillary method --------------------------------------------------------

	public ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration) {
		ModelAndView result;

		result = this.createEditModelAndView(systemConfiguration, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final SystemConfiguration systemConfiguration, final String message) {
		ModelAndView result;

		result = new ModelAndView("systemConfiguration/edit");
		result.addObject("systemConfiguration", systemConfiguration);
		result.addObject("message", message);
		result.addObject("requestURI", "systemConfiguration/administrator/edit.do");

		return result;
	}

}
