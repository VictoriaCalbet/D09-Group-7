
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RequestService;
import services.ServiceService;
import services.UserService;
import controllers.AbstractController;
import domain.Service;
import domain.User;

@Controller
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private UserService		userService;
	@Autowired
	private RequestService	requestService;


	// Constructors ---------------------------------------------------------

	public ServiceUserController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer rendezvousId) {
		ModelAndView result = null;
		Collection<Service> services = null;
		Collection<Service> principalServices = null;
		String requestURI = null;
		String displayURI = null;
		User principalUser = null;

		if (rendezvousId == null)
			services = this.serviceService.findAll();
		else
			services = this.serviceService.findServicesByRendezvousId(rendezvousId);

		requestURI = "service/user/list.do";
		displayURI = "service/user/display.do?serviceId=";

		principalUser = this.userService.findByPrincipal();
		principalServices = this.serviceService.findServicesByUserId(principalUser.getId());

		result = new ModelAndView("service/list");

		result.addObject("principalServices", principalServices);
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
