/*
 * AbstractController.java
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import domain.SystemConfiguration;

@Controller
public class AbstractController {

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@ModelAttribute
	public Model header(final Model model) {
		String siteBanner;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.systemConfigurationService.findMain();
		siteBanner = systemConfiguration.getBannerUrl();
		model.addAttribute("siteBanner", siteBanner);

		return model;
	}

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("redirect:/");
		//result = new ModelAndView("misc/panic");
		//result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		//result.addObject("exception", oops.getMessage());
		//result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}
