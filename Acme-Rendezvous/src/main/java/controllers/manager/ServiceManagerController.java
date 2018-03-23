
package controllers.manager;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ManagerService;
import services.ServiceService;
import services.form.ServiceFormService;
import controllers.AbstractController;
import domain.Category;
import domain.Manager;
import domain.Service;
import domain.form.ServiceForm;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private ServiceFormService	serviceFormService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private CategoryService		categoryService;


	// Constructors ---------------------------------------------------------

	public ServiceManagerController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer rendezvousId, @RequestParam(required = false) final String message) {
		ModelAndView result = null;

		if (rendezvousId == null)
			result = this.listModelAndView(this.serviceService.findAvailableServices(), message);
		else
			result = this.listModelAndView(this.serviceService.findServicesByRendezvousId(rendezvousId), message);

		return result;
	}

	@RequestMapping(value = "/list-created", method = RequestMethod.GET)
	public ModelAndView listCreated() {
		Manager manager = null;

		manager = this.managerService.findByPrincipal();

		return this.listModelAndView(manager.getServices());
	}

	// Creation  ------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		ServiceForm serviceForm = null;

		serviceForm = this.serviceFormService.createFromCreate();
		result = this.createEditModelAndView(serviceForm);

		return result;
	}

	// Display --------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		service = this.serviceService.findOne(serviceId);

		Assert.notNull(service);

		result = new ModelAndView("service/display");
		result.addObject("service", service);
		result.addObject("cancelURI", "/service/manager/list.do");

		return result;
	}

	// Edition    -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView result = null;
		ServiceForm serviceForm = null;
		Service service = null;
		Manager manager = null;

		service = this.serviceService.findOne(serviceId);
		manager = this.managerService.findByPrincipal();

		Assert.isTrue(service.getManager().equals(manager));

		serviceForm = this.serviceFormService.createFromEdit(serviceId);
		result = this.createEditModelAndView(serviceForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ServiceForm serviceForm, final BindingResult bindingResult) {
		ModelAndView result = null;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(serviceForm);
		else
			try {
				if (serviceForm.getId() == 0)
					this.serviceFormService.saveFromCreate(serviceForm);
				else
					this.serviceFormService.saveFromEdit(serviceForm);

				result = new ModelAndView("redirect:/service/manager/list.do");
			} catch (final Throwable oops) {
				String messageError = "service.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(serviceForm, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ServiceForm serviceForm, final BindingResult bindingResult) {
		ModelAndView result = null;
		Service service = null;

		service = this.serviceService.findOne(serviceForm.getId());

		try {
			this.serviceService.delete(service);
			result = new ModelAndView("redirect:/service/manager/list.do");
			result.addObject("message", "service.delete.success");
		} catch (final Throwable oops) {
			String messageError = "service.delete.error";

			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/service/manager/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}

	// Other actions --------------------------------------------------------

	// Ancillary methods ----------------------------------------------------

	protected ModelAndView createEditModelAndView(final ServiceForm serviceForm) {
		ModelAndView result = null;

		result = this.createEditModelAndView(serviceForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ServiceForm serviceForm, final String message) {
		ModelAndView result = null;
		String actionURI = null;
		Collection<Category> categories = null;

		actionURI = "service/manager/edit.do";
		categories = this.categoryService.findAll();

		if (serviceForm.getId() == 0)
			result = new ModelAndView("service/create");
		else
			result = new ModelAndView("service/edit");

		result.addObject("serviceForm", serviceForm);
		result.addObject("categories", categories);
		result.addObject("actionURI", actionURI);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView listModelAndView(final Collection<Service> services) {
		ModelAndView result = null;

		result = this.listModelAndView(services, null);

		return result;
	}
	protected ModelAndView listModelAndView(final Collection<Service> services, final String message) {
		ModelAndView result = null;
		String requestURI = null;
		String displayURI = null;

		requestURI = "service/manager/list.do";
		displayURI = "service/manager/display.do?serviceId=";

		result = new ModelAndView("service/list");
		result.addObject("services", services);
		result.addObject("requestURI", requestURI);
		result.addObject("displayURI", displayURI);
		result.addObject("message", message);

		return result;
	}
}
