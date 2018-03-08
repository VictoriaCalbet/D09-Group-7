
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import controllers.AbstractController;
import domain.Auditor;

@Controller
@RequestMapping("/auditor/administrator")
public class AuditorAdministratorController extends AbstractController {

	// Services
	@Autowired
	private AuditorService	auditorService;


	// Constructor
	public AuditorAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Auditor> auditors;

		auditors = this.auditorService.findAll();

		result = new ModelAndView("auditor/list");
		result.addObject("auditors", auditors);
		result.addObject("requestURI", "auditor/administrator/list.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Auditor auditor;

		auditor = this.auditorService.create();
		result = this.createEditModelAndView(auditor);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Auditor auditor, final BindingResult binding) {
		ModelAndView result;
		boolean bindingError;

		if (binding.hasFieldErrors("folders"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;

		if (bindingError)
			result = this.createEditModelAndView(auditor);
		else
			try {
				this.auditorService.saveFromCreate(auditor);
				result = new ModelAndView("redirect:../..");
			} catch (final Throwable oops) {
				String messageError = "auditor.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(auditor, messageError);
			}

		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Auditor auditor) {
		ModelAndView result;

		result = this.createEditModelAndView(auditor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Auditor auditor, final String message) {
		ModelAndView result;

		result = new ModelAndView("auditor/edit");
		result.addObject("auditor", auditor);
		result.addObject("message", message);
		result.addObject("requestURI", "auditor/administrator/edit.do");

		return result;
	}
}
