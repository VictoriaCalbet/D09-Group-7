
package controllers.manager;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.ManagerService;
import services.TripService;
import controllers.AbstractController;
import domain.Application;
import domain.Manager;
import domain.Trip;

@Controller
@RequestMapping("/application/manager")
public class ApplicationManagerController extends AbstractController {

	//Services
	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private TripService			tripService;

	@Autowired
	private ManagerService		managerService;


	//Constructor

	public ApplicationManagerController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int tripId, @RequestParam(required = false) final String message) {
		final ModelAndView result;

		Trip trip;
		Collection<Application> applications;
		Manager principalManager;

		Date now;
		Date nowPlusAMonth;
		Calendar cal;

		trip = this.tripService.findOne(tripId);
		applications = trip.getApplications();
		principalManager = this.managerService.findByPrincipal();

		now = new Date(System.currentTimeMillis() - 1);
		cal = Calendar.getInstance();
		cal.setTime(now); // your date (java.util.Date)
		cal.add(Calendar.MONTH, +1); // You can -/+ x months here to go back in history or move forward.
		nowPlusAMonth = cal.getTime(); // New date

		result = new ModelAndView("application/list");
		result.addObject("nowPlusAMonth", nowPlusAMonth);
		result.addObject("applications", applications);
		result.addObject("principalManager", principalManager);
		result.addObject("message", message);
		result.addObject("requestURI", "/application/manager/list.do");

		return result;
	}
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int applicationId) {
		ModelAndView result;

		try {
			final Application a = this.applicationService.findOne(applicationId);
			result = this.rejectModelAndView(a);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/trip/manager/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView saveReject(@Valid final Application a, final BindingResult binding) {
		ModelAndView result = new ModelAndView();

		if (binding.hasErrors())
			result = this.rejectModelAndView(a);
		else
			try {
				this.applicationService.rejectApplication(a);
				result = new ModelAndView("redirect:/application/manager/list.do?tripId=" + a.getTrip().getId());
			} catch (final Throwable oops) {
				String messageError = "application.reject.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.rejectModelAndView(a, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/due", method = RequestMethod.GET)
	public ModelAndView due(@RequestParam final int applicationId) {
		ModelAndView result;
		try {
			final Application a = this.applicationService.findOne(applicationId);
			this.applicationService.dueApplication(a);
			result = new ModelAndView("redirect:/application/manager/list.do?tripId=" + a.getTrip().getId());
		} catch (final Throwable oops) {
			String messageError = "application.due.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/trip/manager/list.do");
			result.addObject("message", messageError);
		}
		return result;
	}

	// Ancillaty methods

	protected ModelAndView rejectModelAndView(final Application a) {
		ModelAndView result;

		result = this.rejectModelAndView(a, null);

		return result;
	}

	protected ModelAndView rejectModelAndView(final Application a, final String message) {
		ModelAndView result;

		result = new ModelAndView("application/manager/reject");
		result.addObject("application", a);
		result.addObject("message", message);

		return result;
	}

}
