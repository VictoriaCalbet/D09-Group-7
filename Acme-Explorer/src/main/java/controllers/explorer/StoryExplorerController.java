
package controllers.explorer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ExplorerService;
import services.StoryService;
import services.TripService;
import controllers.AbstractController;
import domain.Explorer;
import domain.Story;
import domain.Trip;

@Controller
@RequestMapping("/story/explorer")
public class StoryExplorerController extends AbstractController {

	//Services
	@Autowired
	private StoryService	storyService;

	@Autowired
	private ExplorerService	explorerService;

	@Autowired
	private TripService		tripService;


	//Constructor
	public StoryExplorerController() {

		super();
	}

	//Listing as a non-admin actor
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final Explorer explorer = this.explorerService.findByPrincipal();
		final Collection<Story> stories = explorer.getStories();
		Boolean noTrips = this.tripService.findEndedTripsWithAcceptedApplication(explorer.getId()).isEmpty();
		result = new ModelAndView("story/explorer/list");
		result.addObject("stories", stories);
		result.addObject("requestURI", "story/explorer/list.do");
		result.addObject("noTrips",noTrips);
		return result;
	}

	//Create a story

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		Story story;

		final Collection<Trip> trips = this.tripService.findEndedTripsWithAcceptedApplication(this.explorerService.findByPrincipal().getId());
		
		try {

			Assert.notNull(trips);
			Assert.notEmpty(trips);
			story = this.storyService.create();
			result = this.createEditModelAndView(story);

		} catch (final Throwable oops) {

			String messageError = "message.error.explorer.noTrips";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			final Explorer explorer = this.explorerService.findByPrincipal();
			final Collection<Story> stories = explorer.getStories();
			Boolean noTrips = this.tripService.findEndedTripsWithAcceptedApplication(explorer.getId()).isEmpty();
			result = new ModelAndView("story/explorer/list");
			result.addObject("stories", stories);
			result.addObject("requestURI", "story/explorer/list.do");
			result.addObject("noTrips",noTrips);
			result.addObject("message", messageError);

		}
		
		return result;
//		if (trips.isEmpty() || trips == null)
//			result = new ModelAndView("redirect:/story/explorer/list.do");
//		else {
//
//			story = this.storyService.create();
//			result = this.createEditModelAndView(story);
//		}
//		return result;

	}

	//Save a note
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Story story, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(story);
		else
			try {

				this.storyService.save(story);

				result = new ModelAndView("redirect:/story/explorer/list.do");
			} catch (final Throwable oops) {

				String messageError = "story.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(story, messageError);

			}
		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final Story story) {

		ModelAndView result;

		result = this.createEditModelAndView(story, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Story story, final String message) {
		ModelAndView result;

		final Collection<Trip> trips = this.tripService.findEndedTripsWithAcceptedApplication(this.explorerService.findByPrincipal().getId());

		result = new ModelAndView("story/explorer/edit");
		result.addObject("story", story);
		result.addObject("message", message);
		result.addObject("trips", trips);

		return result;
	}

}
