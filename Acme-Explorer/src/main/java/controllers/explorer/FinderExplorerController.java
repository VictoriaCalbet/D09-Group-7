
package controllers.explorer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;
import controllers.AbstractController;
import domain.Finder;
import domain.Trip;

@Controller
@RequestMapping("/finder/explorer")
public class FinderExplorerController extends AbstractController {

	// Services --------------------
	@Autowired
	private FinderService	finderService;


	// Constructor -----------------
	public FinderExplorerController() {
		super();
	}
	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		result = new ModelAndView("finder/display");
		Finder finder;
		finder = this.finderService.findFinderByExplorerPrincipal();
		Collection<Trip> trips;
		trips = this.finderService.getTripsFound(finder);
		result.addObject("finder", finder);
		result.addObject("trips", trips);
		result.addObject("requestURI", "finder/explorer/display.do");
		result.addObject("pageSize", 5);
		return result;

	}
	// Edition --------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		try {
			Finder finder;
			finder = this.finderService.findFinderByExplorerPrincipal();
			Assert.notNull(finder);
			result = this.createEditModelAndView(finder);
		} catch (final Throwable oops) {
			result = new ModelAndView("misc/forbidden");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Finder finder, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				this.finderService.save(finder);
				result = new ModelAndView("redirect:display.do");
			} catch (final Throwable oops) {
				String error = "finder.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(finder, error);
			}
		return result;
	}

	// Ancillary methods -------------
	protected ModelAndView createEditModelAndView(final Finder finder) {
		return this.createEditModelAndView(finder, null);
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String message) {
		ModelAndView result;

		result = new ModelAndView("finder/edit");
		result.addObject("finder", finder);
		result.addObject("message", message);

		return result;
	}

}
