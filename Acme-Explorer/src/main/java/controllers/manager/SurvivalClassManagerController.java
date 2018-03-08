
package controllers.manager;

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

import services.ManagerService;
import services.SurvivalClassService;
import services.TripService;
import controllers.AbstractController;
import domain.GPSPoint;
import domain.Location;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@Controller
@RequestMapping("/survivalClass/manager")
public class SurvivalClassManagerController extends AbstractController {

	//Services
	@Autowired
	private SurvivalClassService	survivalClassService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private TripService				tripService;


	//Constructor

	public SurvivalClassManagerController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<SurvivalClass> survivalClasses = new ArrayList<SurvivalClass>();
		Manager principal;

		principal = this.managerService.findByPrincipal();
		final Collection<Trip> managerTrips = principal.getTrips();
		for (final Trip t : managerTrips)
			for (final SurvivalClass sc : t.getSurvivalClasses())
				survivalClasses.add(sc);

		final Collection<Location> locations = new ArrayList<Location>();
		for (final SurvivalClass sc : survivalClasses)
			locations.add((sc.getLocation()));

		final Collection<GPSPoint> gpsPoints = new ArrayList<GPSPoint>();

		for (final Location l : locations)
			gpsPoints.add(l.getGpsPoint());

		result = new ModelAndView("survivalClass/manager/list");//tiles
		result.addObject("survivalClasses", survivalClasses);
		result.addObject("requestURI", "survivalClass/manager/list.do");//view
		result.addObject("principal", principal);
		result.addObject("gpsPoints", gpsPoints);
		return result;

	}

	@RequestMapping(value = "/listByTrip", method = RequestMethod.GET)
	public ModelAndView listByTrip(@RequestParam final int tripId) {
		final ModelAndView result;
		Collection<SurvivalClass> survivalClasses = new ArrayList<SurvivalClass>();
		final Manager principal = this.managerService.findByPrincipal();
		final Trip trip = this.tripService.findOne(tripId);
		//Trips que serán mostrados en la tabla
		survivalClasses = trip.getSurvivalClasses();
		result = new ModelAndView("survivalClass/manager/listByTrip");
		result.addObject("principal", principal);
		result.addObject("survivalClasses", survivalClasses);
		result.addObject("requestURI", "survivalClass/manager/listByTrip.do");
		return result;
	}
	//Creating 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SurvivalClass sc;

		sc = this.survivalClassService.create();
		result = this.createEditModelAndView(sc);

		return result;

	}

	//EDITIONS
	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int survivalClassId) {
		final ModelAndView result;
		SurvivalClass sc;
		sc = this.survivalClassService.findOne(survivalClassId);
		Assert.notNull(sc);
		final Manager principal = this.managerService.findByPrincipal();
		final Trip trip = sc.getTrip();

		Assert.isTrue(trip.getManager().equals(principal), "message.error.survivalClass");
		Assert.isTrue(principal.getTrips().contains(trip), "message.error.survivalClass");

		result = this.createEditModelAndView(sc);
		result.addObject(survivalClassId);

		return result;
	}

	//Saving //TODO
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SurvivalClass sc, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(sc);
		else
			try {
				if (sc.getId() > 0)
					this.survivalClassService.saveFromEdit(sc);

				else
					this.survivalClassService.saveFromCreate(sc);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				String messageError = "survivalClass.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(sc, messageError);
			}

		return result;
	}
	//Deleting//TODO
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SurvivalClass sc, final BindingResult binding) {
		ModelAndView result;

		try {
			final Manager principal = this.managerService.findByPrincipal();
			final Trip trip = sc.getTrip();

			Assert.isTrue(trip.getManager().equals(principal), "message.error.survivalClass");
			Assert.isTrue(principal.getTrips().contains(trip), "message.error.survivalClass");

			this.survivalClassService.delete(sc);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String messageError = "survivalClass.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(sc, messageError);
		}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final SurvivalClass sc) {
		ModelAndView result;

		result = this.createEditModelAndView(sc, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SurvivalClass sc, final String messageCode) {
		ModelAndView result;
		final Manager manager = this.managerService.findByPrincipal();
		final Collection<Trip> trips = manager.getTrips();
		result = new ModelAndView("survivalClass/manager/edit"); //titulo del tiles que le pongas
		result.addObject("survivalClass", sc);
		result.addObject("message", messageCode);
		result.addObject("trips", trips);
		result.addObject("requestURI", "survivalClass/manager/edit.do");
		return result;
	}
}
