
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.Service;

@Controller
@RequestMapping("/service/administrator")
public class ServiceAdministratorController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;


	// Constructors ---------------------------------------------------------

	public ServiceAdministratorController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		ModelAndView result = null;
		Collection<Service> services = null;
		String requestURI = null;
		String displayURI = null;

		services = this.serviceService.findAll();
		requestURI = "service/administrator/list.do";
		displayURI = "service/administrator/display.do?serviceId=";

		result = new ModelAndView("service/list");
		result.addObject("services", services);
		result.addObject("requestURI", requestURI);
		result.addObject("displayURI", displayURI);
		result.addObject("message", message);

		return result;
	}

	// Creation  ------------------------------------------------------------

	// Display --------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		service = this.serviceService.findOne(serviceId);

		result = new ModelAndView();
		result.addObject("service", service);
		result.addObject("cancelURI", "/service/administrator/list.do");

		return result;
	}

	// Edition    -----------------------------------------------------------

	// Other actions --------------------------------------------------------

	@RequestMapping(value = "/markAsInapropiate", method = RequestMethod.GET)
	public ModelAndView markAsInapropiate(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		try {
			service = this.serviceService.findOne(serviceId);

			this.serviceService.markAsInapropiate(service);
			result = new ModelAndView("redirect:/service/administrator/list.do");
			result.addObject("message", "service.markAsInapropiate.success");
		} catch (final Throwable oops) {
			String messageError = "service.markAsInapropiate.error";

			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/service/administrator/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}

	// Ancillary methods ----------------------------------------------------

}