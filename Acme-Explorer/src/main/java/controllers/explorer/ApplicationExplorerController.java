
package controllers.explorer;

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
import services.ExplorerService;
import services.TripService;
import controllers.AbstractController;
import domain.Application;
import domain.Explorer;
import domain.Trip;

@Controller
@RequestMapping("/application/explorer")
public class ApplicationExplorerController extends AbstractController {

	//Services

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ExplorerService		explorerService;

	@Autowired
	private TripService			tripService;


	//Constructor

	public ApplicationExplorerController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Explorer principal;
		Collection<Application> applications;

		Date nowPlusAMonth;
		Calendar calendar;

		principal = this.explorerService.findByPrincipal();
		applications = principal.getApplications();

		calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, +1);
		nowPlusAMonth = calendar.getTime();

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("nowPlusAMonth", nowPlusAMonth);
		result.addObject("requestURI", "application/explorer/list.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		final ModelAndView result;
		Application application;

		application = this.applicationService.create(tripId);
		result = this.createEditModelAndView(application);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Application application, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(application);
		else
			try {
				this.applicationService.saveFromCreate(application);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String messageError = "application.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(application, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		try {
			final Application application = this.applicationService.findOne(applicationId);
			this.applicationService.acceptApplication(application);
			result = new ModelAndView("redirect:/application/explorer/list.do");
			result.addObject("flashMessage", "application.addCreditCard.success");
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/application/explorer/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancelApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		try {
			final Application application = this.applicationService.findOne(applicationId);
			this.applicationService.cancelApplication(application);

			result = new ModelAndView("redirect:/application/explorer/list.do");
			result.addObject("flashMessage", "application.cancelApplication.success");
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/application/explorer/list.do");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final Application application, final String message) {
		ModelAndView result;
		final Collection<Trip> applicableTrips;
		Explorer explorer;

		explorer = this.explorerService.findByPrincipal();
		applicableTrips = this.tripService.findAllApplicableTripsByExplorer(explorer);

		result = new ModelAndView("application/edit");
		result.addObject("application", application);
		result.addObject("message", message);
		result.addObject("trips", applicableTrips);
		result.addObject("requestURI", "application/explorer/edit.do");

		return result;
	}
}
