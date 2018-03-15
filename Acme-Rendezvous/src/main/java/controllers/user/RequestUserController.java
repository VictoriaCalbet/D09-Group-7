
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import services.RequestService;
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
	private RequestService		requestService;
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
	public ModelAndView create(@RequestParam final int serviceId) {
		ModelAndView result;
		RequestForm requestForm;
		requestForm = this.requestFormService.create();
		final Service service = this.serviceService.findOne(serviceId);
		requestForm.setService(service);
		result = this.createEditModelAndView(requestForm);
		final User principal = this.userService.findByPrincipal();
		final Collection<Rendezvous> rendezvousesCreated = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(principal.getId());
		result.addObject("service", service);
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

	//Saving //TODO
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				this.requestFormService.saveFromCreate(requestForm);
				result = new ModelAndView("redirect:/service/user/list.do");

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

}
