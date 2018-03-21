/*
 * AboutUsController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;

@Controller
@RequestMapping("/about-us")
public class AboutUsController extends AbstractController {

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Constructors -----------------------------------------------------------

	public AboutUsController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;
		String businessName;

		businessName = this.systemConfigurationService.findMain().getBusinessName();

		result = new ModelAndView("about-us/index");
		result.addObject("businessName", businessName);

		return result;
	}
}
