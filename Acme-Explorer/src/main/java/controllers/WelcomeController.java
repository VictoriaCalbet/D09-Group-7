/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	//Services
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;
		SystemConfiguration sc;
		String name = "";
		//		String banner;
		String welcomeMessageEnglish;
		String welcomeMessageSpanish;

		sc = this.systemConfigurationService.findMain();
		//		banner = sc.getBannerUrl();
		welcomeMessageEnglish = sc.getWelcomeMessageEnglish();
		welcomeMessageSpanish = sc.getWelcomeMessageSpanish();

		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		result = new ModelAndView("welcome/index");

		if (principal != "anonymousUser")
			name = SecurityContextHolder.getContext().getAuthentication().getName();

		result.addObject("name", name);
		//		result.addObject("banner", banner);
		result.addObject("welcomeMessageEnglish", welcomeMessageEnglish);
		result.addObject("welcomeMessageSpanish", welcomeMessageSpanish);

		return result;
	}
}
