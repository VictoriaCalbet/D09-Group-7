
package controllers.explorer;

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

import services.ContactEmergencyService;
import services.ExplorerService;
import controllers.AbstractController;
import domain.ContactEmergency;
import domain.Explorer;

@Controller
@RequestMapping("/contactEmergency/explorer")
public class ContactEmergencyExplorerController extends AbstractController {

	// Services
	@Autowired
	private ContactEmergencyService	contactEmergencyService;

	@Autowired
	private ExplorerService			explorerService;


	// Constructor
	public ContactEmergencyExplorerController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<ContactEmergency> contactEmergencies;
		Explorer principal;

		principal = this.explorerService.findByPrincipal();
		contactEmergencies = principal.getContactEmergencies();

		result = new ModelAndView("contactEmergency/list");
		result.addObject("contactEmergencies", contactEmergencies);
		result.addObject("requestURI", "contactEmergency/explorer/list.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		ContactEmergency contactEmergency;

		contactEmergency = this.contactEmergencyService.create();
		result = this.createEditModelAndView(contactEmergency);

		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int contactEmergencyId) {
		ModelAndView result;
		ContactEmergency contactEmergency;

		contactEmergency = this.contactEmergencyService.findOne(contactEmergencyId);
		Assert.notNull(contactEmergency);
		result = this.createEditModelAndView(contactEmergency);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ContactEmergency contactEmergency, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(contactEmergency);
		else
			try {
				if (contactEmergency.getId() > 0)
					this.contactEmergencyService.saveFromEdit(contactEmergency);
				else
					this.contactEmergencyService.saveFromCreate(contactEmergency);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String messageError = "contactEmergency.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(contactEmergency, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ContactEmergency contactEmergency, final BindingResult binding) {
		ModelAndView result;

		try {
			this.contactEmergencyService.delete(contactEmergency);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String messageError = "contactEmergency.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(contactEmergency, messageError);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final ContactEmergency contactEmergency) {
		ModelAndView result;

		result = this.createEditModelAndView(contactEmergency, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ContactEmergency contactEmergency, final String message) {
		ModelAndView result;

		result = new ModelAndView("contactEmergency/edit");
		result.addObject("contactEmergency", contactEmergency);
		result.addObject("message", message);

		return result;
	}
}
