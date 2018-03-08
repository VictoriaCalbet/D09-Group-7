/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.FinderService;
import services.SponsorshipService;
import services.TripService;
import domain.Finder;
import domain.Sponsorship;
import domain.Trip;

@Controller
@RequestMapping("/trip")
public class TripController extends AbstractController {

	//Services ----------------------------------------------------------------
	@Autowired
	private TripService			tripService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

	//Listing 

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		ModelAndView result;
		Collection<Trip> trips = new ArrayList<Trip>();
		Map<Integer, Collection<Integer>> tripsToUserAccount = new HashMap<>();

		trips = this.tripService.findAllPublishedAndNotStarted();
		tripsToUserAccount = this.tripService.findExplorersByTrip(trips);

		result = new ModelAndView("trip/list");
		result.addObject("trips", trips);
		result.addObject("message", message);
		result.addObject("tripsToUserAccount", tripsToUserAccount);
		result.addObject("requestURI", "trip/list.do");

		return result;
	}

	//Listing by finder

	@RequestMapping(value = "/listByFinder", method = RequestMethod.GET)
	public ModelAndView listByFinder() {
		ModelAndView result;

		Collection<Trip> trips = new ArrayList<Trip>();
		Map<Integer, Collection<Integer>> tripsToUserAccount = new HashMap<>();

		final Finder finder = this.finderService.findFinderByExplorerPrincipal();

		trips = this.finderService.getTripsFound(finder);
		tripsToUserAccount = this.tripService.findExplorersByTrip(trips);

		result = new ModelAndView("trip/list");
		result.addObject("trips", trips);
		result.addObject("tripsToUserAccount", tripsToUserAccount);
		result.addObject("requestURI", "trip/listByFinder.do");
		result.addObject("message", "finder.commit.ok");

		return result;
	}

	//Listing as a non-admin actor
	@RequestMapping(value = "/listByCategory", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final Integer categoryTripId) {

		final ModelAndView result = new ModelAndView("trip/list");

		Collection<Trip> trips = new HashSet<Trip>();
		Map<Integer, Collection<Integer>> tripsToUserAccount = new HashMap<>();

		try {
			trips = this.categoryService.browseTripsByCategory(this.categoryService.findById(categoryTripId)); // TODO Try-catch
			tripsToUserAccount = this.tripService.findExplorersByTrip(trips);
		} catch (final Throwable oops) {
			String messageError = "trip.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		result.addObject("trips", trips);
		result.addObject("tripsToUserAccount", tripsToUserAccount);
		result.addObject("requestURI", "trip/listByCategory.do");

		return result;
	}

	// Search by key word

	@RequestMapping(value = "/searchWord", method = RequestMethod.POST)
	public ModelAndView searchByKeyWord(@Valid final String word) {

		ModelAndView result;

		Collection<Trip> trips;
		Map<Integer, Collection<Integer>> tripsToUserAccount = new HashMap<>();

		trips = this.tripService.findTripByKeyWord(word);
		tripsToUserAccount = this.tripService.findExplorersByTrip(trips);

		result = new ModelAndView("trip/list");
		result.addObject("trips", trips);
		result.addObject("tripsToUserAccount", tripsToUserAccount);
		result.addObject("requestURI", "trip/list.do");
		return result;

	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ModelAndView info(@RequestParam final int tripId) {
		ModelAndView result = new ModelAndView();

		final Trip trip = this.tripService.findOne(tripId);
		result = this.infoModelAndView(trip);
		return result;
	}

	// Ancillaty methods
	protected ModelAndView infoModelAndView(final Trip trip) {
		ModelAndView result;

		result = this.infoModelAndView(trip, null);

		return result;
	}

	protected ModelAndView infoModelAndView(final Trip trip, final String message) {
		ModelAndView result;

		result = new ModelAndView("trip/info");
		String sponsorshipBanner;
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.getRandomSponsorshipByTripId(trip.getId());

		if (sponsorship != null) {
			sponsorshipBanner = sponsorship.getBannerUrl();
			result.addObject("banner", sponsorshipBanner);

		}
		result.addObject("trip", trip);
		result.addObject("message", message);

		return result;
	}

}
