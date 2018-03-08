
package controllers.user;

import java.util.ArrayList;
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

import services.AnnouncementService;
import services.RendezvousService;
import services.UserService;
import services.form.AnnouncementFormService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;
import domain.form.AnnouncementForm;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private AnnouncementService		announcementService;

	@Autowired
	private AnnouncementFormService	announcementFormService;

	@Autowired
	private UserService				userService;

	@Autowired
	private RendezvousService		rendezvousService;


	// Constructors ---------------------------------------------------------

	public AnnouncementUserController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = null;
		Collection<Announcement> announcements = null;
		Collection<Rendezvous> availableRendezvouses = null;
		User user = null;

		announcements = new ArrayList<Announcement>();
		user = this.userService.findByPrincipal();

		announcements = this.announcementService.getAnnouncementsCreatedByUser(user.getId());
		availableRendezvouses = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId());

		result = new ModelAndView("announcement/list");
		result.addObject("announcements", announcements);
		result.addObject("availableRendezvouses", availableRendezvouses);
		result.addObject("requestURI", "announcement/user/list.do");

		return result;
	}

	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public ModelAndView stream() {
		ModelAndView result = null;
		Collection<Announcement> announcements = null;
		Collection<Rendezvous> availableRendezvouses = null;
		User user = null;

		announcements = new ArrayList<Announcement>();
		user = this.userService.findByPrincipal();

		announcements = this.announcementService.getAnnouncementsPostedAndAcceptedByUser(user.getId());
		availableRendezvouses = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId());

		result = new ModelAndView("announcement/list");
		result.addObject("announcements", announcements);
		result.addObject("availableRendezvouses", availableRendezvouses);
		result.addObject("requestURI", "announcement/user/stream.do");

		return result;
	}

	// Creation  ------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		AnnouncementForm announcementForm = null;

		announcementForm = this.announcementFormService.createFromCreate();
		result = this.createEditModelAndView(announcementForm);

		return result;
	}

	// Display --------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int announcementId) {
		ModelAndView result = null;
		Announcement announcement = null;
		String cancelURI = null;

		cancelURI = "/";
		announcement = this.announcementService.findOne(announcementId);

		result = new ModelAndView("announcement/display");
		result.addObject("announcement", announcement);
		result.addObject("cancelURI", cancelURI);

		return result;
	}

	// Edition    -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int announcementId) {
		ModelAndView result = null;
		AnnouncementForm announcementForm = null;
		Announcement announcement = null;
		User user = null;

		announcement = this.announcementService.findOne(announcementId);
		user = this.userService.findByPrincipal();

		Assert.isTrue(announcement.getRendezvous().getCreator().equals(user));

		announcementForm = this.announcementFormService.createFromEdit(announcementId);
		result = this.createEditModelAndView(announcementForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final AnnouncementForm announcementForm, final BindingResult binding) {
		ModelAndView result = null;

		if (binding.hasErrors())
			result = this.createEditModelAndView(announcementForm);
		else
			try {
				if (announcementForm.getId() == 0)
					this.announcementFormService.saveFromCreate(announcementForm);
				else
					this.announcementFormService.saveFromEdit(announcementForm);

				result = new ModelAndView("redirect:/announcement/list.do?rendezvousId=" + announcementForm.getRendezvousId());
			} catch (final Throwable oops) {
				String messageError = "announcement.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(announcementForm, messageError);
			}

		return result;
	}

	// Ancillary methods ----------------------------------------------------

	protected ModelAndView createEditModelAndView(final AnnouncementForm announcementForm) {
		ModelAndView result = null;
		result = this.createEditModelAndView(announcementForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final AnnouncementForm announcementForm, final String message) {
		ModelAndView result = null;
		User user = null;
		Collection<Rendezvous> rendezvouses = null;
		String requestURI = null;

		user = this.userService.findByPrincipal();
		rendezvouses = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId());
		requestURI = "announcement/user/edit.do";

		if (announcementForm.getId() == 0)
			result = new ModelAndView("announcement/create");
		else {
			Rendezvous rendezvous = null;
			rendezvous = this.rendezvousService.findOne(announcementForm.getRendezvousId());
			result = new ModelAndView("announcement/edit");
			result.addObject("rendezvousName", rendezvous.getName());
		}

		result.addObject("announcementForm", announcementForm);
		result.addObject("availableRendezvouses", rendezvouses);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;
	}
}
