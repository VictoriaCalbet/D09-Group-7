
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.TripService;
import domain.Story;

@Controller
@RequestMapping("/story")
public class StoryController extends AbstractController {

	//Services

	//Constructor
	public StoryController() {

		super();
	}


	@Autowired
	private TripService	tripService;


	//Listing as a non-admin actor
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final Integer tripId) {

		ModelAndView result;
		final Collection<Story> stories = this.tripService.findOne(tripId).getStories();
		result = new ModelAndView("story/list");
		result.addObject("stories", stories);
		result.addObject("requestURI", "story/list.do");

		return result;
	}

}
