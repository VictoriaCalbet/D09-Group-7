
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.form.ManagerFormService;
import domain.form.ManagerForm;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	@Autowired
	private ManagerFormService	managerFormService;


	public ManagerController() {
		super();
	}

	// Creation ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		ManagerForm managerForm;

		managerForm = this.managerFormService.create();
		result = this.createEditModelAndView(managerForm);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(managerForm);
		else
			try {
				if (managerForm.getId() != 0)
					this.managerFormService.saveFromEdit(managerForm);
				else
					this.managerFormService.saveFromCreate(managerForm);
				result = new ModelAndView("redirect:../");
			} catch (final Throwable oops) {
				String messageError = "manager.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(managerForm, messageError);
			}

		return result;
	}

	// Ancillaty methods

	public ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(managerForm, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final ManagerForm managerForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("manager/edit");
		result.addObject("managerForm", managerForm);
		result.addObject("message", message);
		result.addObject("requestURI", "manager/edit.do");

		return result;
	}
}
