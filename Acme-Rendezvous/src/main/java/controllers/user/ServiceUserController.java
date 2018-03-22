
package controllers.user;

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
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;


	// Constructors ---------------------------------------------------------

	public ServiceUserController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer rendezvousId) {
		ModelAndView result = null;
		Collection<Service> services = null;
		String requestURI = null;
		String displayURI = null;

		if (rendezvousId == null)
			services = this.serviceService.findAvailableServices();
		else
			services = this.serviceService.findServicesByRendezvousId(rendezvousId);

		requestURI = "service/user/list.do";
		displayURI = "service/user/display.do?serviceId=";

		result = new ModelAndView("service/list");

		result.addObject("services", services);
		result.addObject("requestURI", requestURI);
		result.addObject("displayURI", displayURI);

		return result;
	}
	// Creation  ------------------------------------------------------------

	// Display --------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		service = this.serviceService.findOne(serviceId);

		result = new ModelAndView("service/display");
		result.addObject("service", service);
		result.addObject("cancelURI", "/service/user/list.do");

		return result;
	}

	// Edition    -----------------------------------------------------------

	// Ancillary methods ----------------------------------------------------
}
