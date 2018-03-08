
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/administrator")
public class AnnouncementAdministratorController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;


	// Constructors ---------------------------------------------------------

	public AnnouncementAdministratorController() {
		super();
	}

	// Listing --------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		ModelAndView result = null;
		Collection<Announcement> announcements = null;

		announcements = new ArrayList<Announcement>();
		announcements = this.announcementService.findAll();

		result = new ModelAndView("announcement/list");
		result.addObject("announcements", announcements);
		result.addObject("message", message);
		result.addObject("requestURI", "announcement/administrator/list.do");

		return result;
	}

	// Creation  ------------------------------------------------------------

	// Display --------------------------------------------------------------

	// Edition    -----------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int announcementId) {
		ModelAndView result = null;
		Announcement announcement = null;

		try {
			announcement = this.announcementService.findOne(announcementId);

			this.announcementService.delete(announcement);
			result = new ModelAndView("redirect:/announcement/administrator/list.do");
			result.addObject("message", "announcement.delete.success");
		} catch (final Throwable oops) {
			String messageError = "announcement.delete.error";

			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/announcement/administrator/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}

	// Ancillary methods ----------------------------------------------------

	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		ModelAndView result = null;
		result = this.createEditModelAndView(announcement, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Announcement announcement, final String message) {
		ModelAndView result = null;

		result = new ModelAndView("announcement/edit");
		result.addObject("announcement", announcement);
		result.addObject("message", message);

		return result;
	}
}
