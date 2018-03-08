
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

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
import services.UserService;
import services.form.RendezvousFormService;
import services.form.RendezvousLinkedFormService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.User;
import domain.form.RendezvousForm;
import domain.form.RendezvousLinkedForm;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	@Autowired
	private RendezvousService			rendezvousService;

	@Autowired
	private RendezvousFormService		rendezvousFormService;

	@Autowired
	private UserService					userService;

	@Autowired
	private RendezvousLinkedFormService	rendezvousLinkedFormService;


	//	@Autowired
	//	private Validator				rendezvousFormValidator;

	// Constructors -----------------------------------------------------------

	public RendezvousUserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses = new ArrayList<Rendezvous>();
		final User u = this.userService.findByPrincipal();
		rendezvouses = u.getRendezvoussesCreated();

		//Assist button control
		Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();
		principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(u.getId());
		result = new ModelAndView("rendezvous/list");
		result.addObject("principalRendezvouses", principalRendezvouses);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("message", message);
		result.addObject("requestURI", "rendezvous/user/list.do");

		return result;
	}

	// Creation ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		RendezvousForm rendezvousForm;

		rendezvousForm = this.rendezvousFormService.create();
		result = this.createModelAndView(rendezvousForm);
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@Valid final RendezvousForm rendezvousForm, final BindingResult binding) {
		//		this.rendezvousFormValidator.validate(rendezvousForm, binding);

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(rendezvousForm);
		else
			try {
				this.rendezvousFormService.saveFromCreate(rendezvousForm);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
			} catch (final Throwable oops) {
				String messageError = "rendezvous.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createModelAndView(rendezvousForm, messageError);
			}

		return result;
	}

	// Edit ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		RendezvousForm rendezvousForm;

		try {
			rendezvousForm = this.rendezvousFormService.create(rendezvousId);
			result = this.editModelAndView(rendezvousForm);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/rendezvous/user/list.do");
			result.addObject("message", oops.getMessage());
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final RendezvousForm rendezvousForm, final BindingResult binding) {
		//		this.rendezvousFormValidator.validate(rendezvousForm, binding);

		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(rendezvousForm);
		else
			try {
				this.rendezvousFormService.saveFromEdit(rendezvousForm);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
			} catch (final Throwable oops) {
				String messageError = "rendezvous.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.editModelAndView(rendezvousForm, messageError);
			}

		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int rendezvousId) {
		ModelAndView result;
		try {
			this.rendezvousService.delete(rendezvousId);
			result = new ModelAndView("redirect:/rendezvous/user/list.do");
		} catch (final Throwable oops) {
			String messageError = "rendezvous.delete.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/rendezvous/user/list.do");
			result.addObject("message", messageError);
		}
		return result;
	}

	// Linked ----------------------------------------------------------------

	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public ModelAndView link(@RequestParam final int rendezvousId) {
		ModelAndView result;
		try {

			RendezvousLinkedForm rendezvousLinkedForm;

			rendezvousLinkedForm = this.rendezvousLinkedFormService.create(rendezvousId);
			result = this.createLinkModelAndView(rendezvousLinkedForm);
		} catch (final Throwable oops) {
			String messageError = "";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/rendezvous/user/list.do");
			result.addObject("message", messageError);
		}
		return result;

	}

	@RequestMapping(value = "/link", method = RequestMethod.POST, params = "save")
	public ModelAndView link(@Valid final RendezvousLinkedForm rendezvousLinkedForm, final BindingResult binding) {
		//		this.rendezvousFormValidator.validate(rendezvousForm, binding);

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createLinkModelAndView(rendezvousLinkedForm);
		else
			try {
				this.rendezvousLinkedFormService.linkedTo(rendezvousLinkedForm);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
			} catch (final Throwable oops) {
				String messageError = "rendezvous.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createLinkModelAndView(rendezvousLinkedForm, messageError);
			}

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createModelAndView(final RendezvousForm rendezvousForm) {
		ModelAndView result;

		result = this.createModelAndView(rendezvousForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RendezvousForm rendezvousForm, final String message) {
		ModelAndView result;

		final User u = this.userService.findByPrincipal();

		final GregorianCalendar g = new GregorianCalendar();
		if (u.getBirthDate() != null)
			g.setTime(u.getBirthDate());
		final int age = this.rendezvousService.calculateAge(g);

		result = new ModelAndView("rendezvous/user/create");
		result.addObject("rendezvousForm", rendezvousForm);
		result.addObject("message", message);
		result.addObject("age", age);

		return result;
	}

	protected ModelAndView editModelAndView(final RendezvousForm rendezvousForm) {
		ModelAndView result;

		result = this.editModelAndView(rendezvousForm, null);

		return result;
	}

	protected ModelAndView editModelAndView(final RendezvousForm rendezvousForm, final String message) {
		ModelAndView result;

		final User u = this.userService.findByPrincipal();

		final GregorianCalendar g = new GregorianCalendar();
		if (u.getBirthDate() != null)
			g.setTime(u.getBirthDate());
		final int age = this.rendezvousService.calculateAge(g);

		result = new ModelAndView("rendezvous/user/edit");
		result.addObject("rendezvousForm", rendezvousForm);
		result.addObject("message", message);
		result.addObject("age", age);

		return result;
	}

	protected ModelAndView createLinkModelAndView(final RendezvousLinkedForm rendezvousLinkedForm) {
		ModelAndView result;

		result = this.createLinkModelAndView(rendezvousLinkedForm, null);

		return result;
	}

	protected ModelAndView createLinkModelAndView(final RendezvousLinkedForm rendezvousLinkedForm, final String message) {
		ModelAndView result;

		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousLinkedForm.getRendezvousId());
		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousSimilarNotDeleted();
		rendezvouses.removeAll(this.rendezvousService.findRendezvousSimilar(rendezvousLinkedForm.getRendezvousId()));
		rendezvouses.remove(rendezvous);
		Assert.isTrue(rendezvouses.size() > 0, "message.error.rendezvous.isLinkedTo.size");

		result = new ModelAndView("rendezvous/user/link");
		result.addObject("rendezvousLinkedForm", rendezvousLinkedForm);
		result.addObject("message", message);
		result.addObject("rendezvouses", rendezvouses);

		return result;
	}
}
