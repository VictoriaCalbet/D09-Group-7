
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import controllers.AbstractController;
import domain.Administrator;

@Controller
@RequestMapping("/administrator/administrator")
public class AdministratorAdministratorController extends AbstractController {

	// Services
	@Autowired
	private AdministratorService	administratorService;


	// Constructor
	public AdministratorAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Administrator> administrators;

		administrators = this.administratorService.findAll();

		result = new ModelAndView("administrator/list");
		result.addObject("administrators", administrators);
		result.addObject("requestURI", "administrator/administrator/list.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.create();
		result = this.createEditModelAndView(administrator);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.findByPrincipal();
		Assert.notNull(administrator);
		result = this.createEditModelAndView(administrator);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Administrator administrator, final BindingResult binding) {
		ModelAndView result;
		boolean bindingError;

		if (binding.hasFieldErrors("folders"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;

		if (bindingError)
			result = this.createEditModelAndView(administrator);
		else
			try {
				if (administrator.getId() > 0)
					this.administratorService.saveFromEditWithEncoding(administrator);
				else
					this.administratorService.saveFromCreate(administrator);
				result = new ModelAndView("redirect:../..");
			} catch (final Throwable oops) {
				String messageError = "administrator.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(administrator, messageError);
			}

		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Administrator administrator) {
		ModelAndView result;

		result = this.createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator administrator, final String message) {
		ModelAndView result;

		result = new ModelAndView("administrator/edit");
		result.addObject("administrator", administrator);
		result.addObject("message", message);

		return result;
	}
}
