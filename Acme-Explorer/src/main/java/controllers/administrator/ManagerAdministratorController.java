
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import controllers.AbstractController;
import domain.Manager;

@Controller
@RequestMapping("/manager/administrator")
public class ManagerAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ManagerService	managerService;


	// Constructor
	public ManagerAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Manager> managers;

		managers = this.managerService.findAll();

		result = new ModelAndView("manager/list");
		result.addObject("managers", managers);
		result.addObject("requestURI", "manager/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/listSuspicious", method = RequestMethod.GET)
	public ModelAndView listSuspicious() {
		ModelAndView result;
		Collection<Manager> managers;

		managers = this.managerService.findAllSuspicious();

		result = new ModelAndView("manager/list");
		result.addObject("managers", managers);
		result.addObject("requestURI", "manager/administrator/listSuspicious.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Manager manager;

		manager = this.managerService.create();
		result = this.createEditModelAndView(manager);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Manager manager, final BindingResult binding) {
		ModelAndView result;
		boolean bindingError;

		if (binding.hasFieldErrors("folders"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;

		if (bindingError)
			result = this.createEditModelAndView(manager);
		else
			try {
				this.managerService.saveFromCreate(manager);
				result = new ModelAndView("redirect:../..");
			} catch (final Throwable oops) {
				String messageError = "manager.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(manager, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(final int managerId) {
		final ModelAndView result = new ModelAndView("manager/list");
		Collection<Manager> managers;

		try {
			this.managerService.ban(managerId);
		} catch (final Throwable oops) {
			String messageError = "manager.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		managers = this.managerService.findAllSuspicious();
		result.addObject("managers", managers);
		result.addObject("requestURI", "manager/administrator/listSuspicious.do");

		return result;

	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(final int managerId) {
		final ModelAndView result = new ModelAndView("manager/list");
		Collection<Manager> managers;

		try {
			this.managerService.unban(managerId);
		} catch (final Throwable oops) {
			String messageError = "manager.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		managers = this.managerService.findAllSuspicious();
		result.addObject("managers", managers);
		result.addObject("requestURI", "manager/administrator/listSuspicious.do");

		return result;

	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Manager manager) {
		ModelAndView result;

		result = this.createEditModelAndView(manager, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Manager manager, final String message) {
		ModelAndView result;

		result = new ModelAndView("manager/edit");
		result.addObject("manager", manager);
		result.addObject("message", message);
		result.addObject("requestURI", "manager/administrator/edit.do");

		return result;
	}
}
