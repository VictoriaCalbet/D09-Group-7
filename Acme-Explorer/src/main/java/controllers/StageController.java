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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.TripService;
import domain.Stage;
import domain.Trip;

@Controller
@RequestMapping("/stage")
public class StageController extends AbstractController {

	//Services ----------------------------------------------------------------

	@Autowired
	private TripService	tripService;


	// Constructors -----------------------------------------------------------

	public StageController() {
		super();
	}

	//Listing 

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int tripId, @RequestParam(required = false) final String message) {
		final ModelAndView result;
		Collection<Stage> stages = new ArrayList<Stage>();

		final Trip t = this.tripService.findOne(tripId);
		stages = t.getStages();

		result = new ModelAndView("stage/list");
		result.addObject("stages", stages);
		result.addObject("message", message);
		result.addObject("requestURI", "stage/list.do");
		result.addObject("t", t);
		result.addObject("tripId", tripId);

		return result;
	}

}
