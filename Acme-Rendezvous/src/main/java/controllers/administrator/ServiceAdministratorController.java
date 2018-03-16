
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
	public ModelAndView list(@RequestParam(required = false) final Integer rendezvousId, @RequestParam(required = false) final String message) {
		ModelAndView result = null;
		Collection<Service> services = null;
		String requestURI = null;
		String displayURI = null;

		if (rendezvousId == null)
			services = this.serviceService.findAll();
		else
			services = this.serviceService.findServicesByRendezvousId(rendezvousId);

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

		result = new ModelAndView("service/display");
		result.addObject("service", service);
		result.addObject("cancelURI", "/service/administrator/list.do");

		return result;
	}

	// Edition    -----------------------------------------------------------

	// Other actions --------------------------------------------------------

	@RequestMapping(value = "/markAsInappropriate", method = RequestMethod.GET)
	public ModelAndView markAsInappropriate(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		try {
			service = this.serviceService.findOne(serviceId);

			if (service == null) {
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "message.error.service.null");
			} else if (service.getIsInappropriate() != true) {
				this.serviceService.markingServiceAsAppropriateOrNot(service, true);
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "service.markAsInappropriate.success");
			} else {
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "service.markAsInappropriate.itsAlreadyMarked");
			}
		} catch (final Throwable oops) {
			String messageError = "service.markAsInappropriate.error";

			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/service/administrator/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}
	@RequestMapping(value = "/unmarkAsInappropriate", method = RequestMethod.GET)
	public ModelAndView unmarkAsInappropriate(@RequestParam final int serviceId) {
		ModelAndView result = null;
		Service service = null;

		try {
			service = this.serviceService.findOne(serviceId);

			if (service == null) {
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "message.error.service.null");
			} else if (service.getIsInappropriate() != false) {
				this.serviceService.markingServiceAsAppropriateOrNot(service, false);
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "service.unmarkAsInappropriate.success");
			} else {
				result = new ModelAndView("redirect:/service/administrator/list.do");
				result.addObject("message", "service.unmarkAsInappropriate.itsAlreadyMarked");
			}
		} catch (final Throwable oops) {
			String messageError = "service.unmarkAsInappropriate.error";

			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/service/administrator/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}

	// Ancillary methods ----------------------------------------------------

}
