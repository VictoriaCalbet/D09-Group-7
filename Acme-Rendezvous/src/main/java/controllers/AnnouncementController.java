
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.RendezvousService;
import domain.Announcement;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors ---------------------------------------------------------

	public AnnouncementController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result = null;
		Collection<Announcement> announcements = null;

		announcements = this.rendezvousService.findOne(rendezvousId).getAnnouncements();

		result = new ModelAndView();
		result.addObject("announcements", announcements);
		result.addObject("requestURI", "announcement/list.do");

		return result;
	}
	// Creation  ------------------------------------------------------------

	// Display --------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int announcementId) {
		ModelAndView result = null;
		Announcement announcement = null;

		announcement = this.announcementService.findOne(announcementId);

		result = new ModelAndView();
		result.addObject("announcement", announcement);
		result.addObject("cancelURI", "/"); // TODO: cancelURI

		return result;
	}

	// Edition    -----------------------------------------------------------

	// Ancillary methods ----------------------------------------------------
}
