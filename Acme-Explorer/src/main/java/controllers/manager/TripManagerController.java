
package controllers.manager;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.LegalTextService;
import services.ManagerService;
import services.RangerService;
import services.TripService;
import controllers.AbstractController;
import domain.Category;
import domain.LegalText;
import domain.Manager;
import domain.Ranger;
import domain.Trip;

@Controller
@RequestMapping("/trip/manager")
public class TripManagerController extends AbstractController {

	//Services
	@Autowired
	private ManagerService		managerService;
	@Autowired
	private TripService			tripService;
	@Autowired
	private RangerService		rangerService;
	@Autowired
	private LegalTextService	legalTextService;
	@Autowired
	private CategoryService		categoryService;


	//Constructor

	public TripManagerController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		final ModelAndView result;
		Collection<Trip> trips;
		final Manager m = this.managerService.findByPrincipal();
		trips = m.getTrips();

		result = new ModelAndView("trip/manager/list");
		result.addObject("trips", trips);
		result.addObject("message", message);
		result.addObject("requestURI", "trip/manager/list.do");
		return result;

	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Trip trip;

		trip = this.tripService.create();
		result = this.createModelAndView(trip);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tripId) {
		ModelAndView result = new ModelAndView();

		try {
			final Trip trip = this.tripService.findOne(tripId);
			Assert.isTrue(this.managerService.findByPrincipal().getTrips().contains(trip));
			result = this.editModelAndView(trip);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/trip/manager/list.do");
		}

		return result;
	}

	// Cancel
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int tripId) {
		ModelAndView result;
		try {
			final Trip trip = this.tripService.findOne(tripId);
			Assert.isTrue(this.managerService.findByPrincipal().getTrips().contains(trip));
			result = this.cancelModelAndView(trip);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/trip/manager/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Trip trip, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(trip);
		else
			try {
				this.tripService.save(trip);
				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {
				String messageError = "trip.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createModelAndView(trip, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@Valid final Trip trip, final BindingResult binding) {
		ModelAndView result = new ModelAndView();

		if (binding.hasErrors())
			result = this.editModelAndView(trip);
		else
			try {
				this.tripService.update(trip);
				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {
				String messageError = "trip.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.editModelAndView(trip, messageError);
			}

		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int tripId) {
		ModelAndView result;
		try {
			this.tripService.delete(tripId);
			result = new ModelAndView("redirect:/trip/manager/list.do");
		} catch (final Throwable oops) {
			String messageError = "trip.delete.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/trip/manager/list.do");
			result.addObject("message", messageError);
		}
		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCancel(@Valid final Trip trip, final BindingResult binding) {
		ModelAndView result = new ModelAndView();

		if (binding.hasErrors())
			result = this.cancelModelAndView(trip);
		else
			try {
				this.tripService.cancelledTrip(trip.getId(), trip.getReason());
				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {
				String messageError = "trip.cancel.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.cancelModelAndView(trip, messageError);
			}

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createModelAndView(final Trip trip) {
		ModelAndView result;

		result = this.createModelAndView(trip, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Trip trip, final String message) {
		ModelAndView result;

		final Collection<Ranger> rangers = this.rangerService.findAllNotBanned();
		final Collection<LegalText> legalTexts = this.legalTextService.getFinalLegalTexts();
		final Collection<Category> categories = this.categoryService.findAllWithOutCATEGORY();
		final Map<Integer, String> map = this.categoryService.createCategoryLabels(categories);
		
		
		result = new ModelAndView("trip/manager/create");
		result.addObject("trip", trip);
		result.addObject("rangers", rangers);
		result.addObject("legalTexts", legalTexts);
		result.addObject("categories", categories);
		result.addObject("message", message);
		result.addObject("map",map);

		return result;
	}

	protected ModelAndView editModelAndView(final Trip trip) {
		ModelAndView result;

		result = this.editModelAndView(trip, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Trip trip, final String message) {
		ModelAndView result;

		final Collection<Ranger> rangers = this.rangerService.findAllNotBanned();
		final Collection<LegalText> legalTexts = this.legalTextService.getFinalLegalTexts();
		final Collection<Category> categories = this.categoryService.findAllWithOutCATEGORY();

		result = new ModelAndView("trip/manager/edit");
		result.addObject("trip", trip);
		result.addObject("rangers", rangers);
		result.addObject("legalTexts", legalTexts);
		result.addObject("categories", categories);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView cancelModelAndView(final Trip trip) {
		ModelAndView result;

		result = this.cancelModelAndView(trip, null);

		return result;
	}

	protected ModelAndView cancelModelAndView(final Trip trip, final String message) {
		ModelAndView result;

		result = new ModelAndView("trip/manager/cancel");
		result.addObject("trip", trip);
		result.addObject("message", message);

		return result;
	}

}
