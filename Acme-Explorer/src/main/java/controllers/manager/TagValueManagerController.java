
package controllers.manager;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import services.TagService;
import services.TagValueService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@Controller
@RequestMapping("/tagValue/manager")
public class TagValueManagerController extends AbstractController {

	// Services
	@Autowired
	private TagValueService	tagValueService;
	@Autowired
	private TagService		tagService;
	@Autowired
	private TripService		tripService;
	@Autowired
	private ManagerService	managerService;


	// Constructor
	public TagValueManagerController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tagId) {
		ModelAndView result;
		TagValue tagValue;

		tagValue = this.tagValueService.createFromTag(tagId);
		result = this.createEditModelAndView(tagValue);
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tagValueId) {
		ModelAndView result = new ModelAndView();

		final TagValue tagValue = this.tagValueService.findOne(tagValueId);
		result = this.createEditModelAndView(tagValue);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final TagValue tagValue, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(tagValue);
		else
			try {
				if (tagValue.getId() > 0)
					this.tagValueService.saveFromEdit(tagValue);
				else
					this.tagValueService.saveFromCreate(tagValue);
				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {
				String messageError = "tagValue.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(tagValue, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final TagValue tagValue, final BindingResult binding) {
		ModelAndView result;

		try {
			this.tagValueService.delete(tagValue);
			result = new ModelAndView("redirect:/trip/manager/list.do");
		} catch (final Throwable oops) {
			String messageError = "tagValue.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(tagValue, messageError);
		}

		return result;
	}

	// Ancillary methods
	public ModelAndView createEditModelAndView(final TagValue tagValue) {
		ModelAndView result;

		result = this.createEditModelAndView(tagValue, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final TagValue tagValue, final String message) {
		ModelAndView result;

		final Collection<Tag> tags = this.tagService.findAll();

		Manager principal;
		principal = this.managerService.findByPrincipal();

		final Collection<Trip> trips = this.tripService.findAllNotPublishedByManagerId(principal.getId());

		// DO NOT DELETE THIS CODE.
		// Due to the lazy retrieval of the Collection  by Hibernate and
		// the fact that Hibernate sessions (transactions) close before JSP charges,
		// when you came back from an error, the groupOfValues was null and an HTTP Error 500 was shown.
		Collection<String> groupOfValues = new HashSet<>();

		if (tagValue.getTag() != null) {
			final Tag tag = this.tagService.findOne(tagValue.getTag().getId());
			groupOfValues = tag.getGroupOfValues();
		}

		result = new ModelAndView("tagValue/edit");
		result.addObject("tagValue", tagValue);
		result.addObject("message", message);
		result.addObject("tags", tags);
		result.addObject("trips", trips);
		result.addObject("groupOfValues", groupOfValues);

		return result;
	}

}
