
package controllers.user;

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

import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import services.form.RequestFormService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.Service;
import domain.User;
import domain.form.RequestForm;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	@Autowired
	private RequestFormService	requestFormService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public RequestUserController() {
		super();
	}

	//Creating 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		RequestForm requestForm;
		requestForm = this.requestFormService.create();
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		requestForm.setRendezvous(rendezvous);
		result = this.createEditModelAndView(requestForm);
		final User principal = this.userService.findByPrincipal();
		final Collection<Rendezvous> rendezvousesCreated = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(principal.getId());
		final Collection<Service> availableServices = this.serviceService.findAvailableServicesToRequest(rendezvousId);

		try {
			Assert.notEmpty(this.serviceService.findAvailableServicesToRequest(rendezvous.getId()), "message.error.noAvailableServices");

		} catch (final Throwable oops) {
			String messageError = "message.error.noAvailableServices";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.listModelAndView("redirect:/rendezvous/user/list.do", messageError);

		}

		result.addObject("rendezvous", rendezvous);
		result.addObject("availableServices", availableServices);
		result.addObject("rendezvousesCreated", rendezvousesCreated);
		return result;

	}
	//EDITIONS
	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int requestId) {
		final ModelAndView result;
		RequestForm requestForm;
		requestForm = this.requestFormService.create(requestId);

		result = this.createEditModelAndView(requestForm);
		result.addObject(requestId);

		return result;
	}

	//Saving
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				this.requestFormService.saveFromCreate(requestForm);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");

			} catch (final Throwable oops) {
				String messageError = "request.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(requestForm, messageError);
			}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final RequestForm requestForm) {
		ModelAndView result;

		result = this.createEditModelAndView(requestForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestForm requestForm, final String messageCode) {
		ModelAndView result;
		final Rendezvous rendezvous = requestForm.getRendezvous();
		final Service service = requestForm.getService();
		result = new ModelAndView("request/user/edit");
		result.addObject("requestForm", requestForm);
		result.addObject("service", service);
		result.addObject("rendezvous", rendezvous);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "request/user/edit.do");
		return result;
	}

	protected ModelAndView listModelAndView(final String string) {
		ModelAndView result;

		result = this.listModelAndView(string, null);

		return result;
	}

	protected ModelAndView listModelAndView(final String string, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("redirect:/rendezvous/user/list.do");
		result.addObject("string", string);

		result.addObject("message", messageCode);
		//		result.addObject("requestURI", "rendezvous/user/edit.do");
		return result;
	}

}
