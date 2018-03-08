
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.form.ActorFormService;
import controllers.AbstractController;
import domain.Administrator;
import domain.form.ActorForm;

@Controller
@RequestMapping("/administrator/administrator")
public class AdministratorAdministratorController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorFormService		actorFormService;


	// Constructors -----------------------------------------------------------

	public AdministratorAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------

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

	// Creation ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		ActorForm actorForm;

		actorForm = this.actorFormService.create();
		result = this.createEditModelAndView(actorForm);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		ActorForm actorForm;

		actorForm = this.actorFormService.createFromActor();
		result = this.createEditModelAndView(actorForm);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				if (actorForm.getId() != 0)
					this.actorFormService.saveFromEdit(actorForm, "ADMIN");
				else
					this.actorFormService.saveFromCreate(actorForm, "ADMIN");
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String messageError = "administrator.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(actorForm, messageError);
			}

		return result;
	}
	// Ancillary method --------------------------------------------------------

	public ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("actorForm/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("requestURI", "administrator/administrator/edit.do");

		return result;
	}
}
