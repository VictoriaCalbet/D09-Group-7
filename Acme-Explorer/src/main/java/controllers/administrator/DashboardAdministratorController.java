
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.AuditService;
import services.LegalTextService;
import services.ManagerService;
import services.NoteService;
import services.RangerService;
import services.TripService;
import controllers.AbstractController;
import domain.LegalText;
import domain.Trip;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services

	@Autowired
	private ApplicationService	applicationService;
	@Autowired
	private TripService			tripService;
	@Autowired
	private ManagerService		managerService;

	@Autowired
	private RangerService		rangerService;
	@Autowired
	private LegalTextService	legalTextService;
	@Autowired
	private NoteService			noteService;
	@Autowired
	private AuditService		auditService;


	// Constructor
	public DashboardAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		double averageApplicationsPerTrip;
		averageApplicationsPerTrip = this.applicationService.findAverageApplicationPerTrip();
		final Integer minimumApplicationsPerTrip = this.applicationService.findMinApplicationPerTrip();
		final Integer maximumApplicationsPerTrip = this.applicationService.findMaxApplicationPerTrip();
		final double standardDeviationApplicationsPerTrip = this.applicationService.findStandardDeviationOfApplicationPerTrip();

		final double averageTripsPerManager = this.managerService.findAverageTripsPerManager();
		final Integer minimumTripsPerManager = this.managerService.findMinTripsPerManager();
		final Integer maximumTripsPerManager = this.managerService.findMaxTripsPerManager();
		final double standardDeviationOfTripPerManager = this.managerService.findAverageTripsPerManager();

		final double averagePriceOfTrips = this.tripService.findAveragePriceOfTrips();
		final double minimumPriceOfTrips = this.tripService.findMinPriceOfTrips();
		final double maximumPriceOfTrips = this.tripService.findMaxPriceOfTrips();
		final double standardDeviationPriceOfTrips = this.tripService.findStantardDeviationOfPriceOfTrips();
		final double averageTripsPerRanger = this.rangerService.findAverageTripPerRanger();
		final Integer minimumTripsPerRanger = this.rangerService.findMinTripsPerRanger();
		final Integer maximumTripsPerRanger = this.rangerService.findMaxTripsPerRanger();
		final double standardDeviationTripsPerRanger = this.rangerService.findStandardDeviationOfTripsPerRanger();
		final double ratioApplicationsWithPending = this.applicationService.findRatioOfApplicationsWithPending();
		final double ratioApplicationsWithDue = this.applicationService.findRatioOfApplicationsWithDue();
		final double ratioApplicationsWithAccepted = this.applicationService.findRatioOfApplicationsWithAccepted();
		final double ratioApplicationsWithCancelled = this.applicationService.findRatioOfApplicationsWithCancelled();
		final double ratioCancelledTripsVersusTotal = this.tripService.findRatioOfTripsVersusNumberOfTrips();
		final Collection<Trip> nameTripsWith10MoreThanAverageOrdered = this.tripService.findTripsWithAverageHigherThan10PerCentOrderedByApplicationNumber();

		final Collection<LegalText> legalTextName = this.legalTextService.legalTextName();
		final double averageNumberNotesPerTrip = this.noteService.findAverageNumberOfNotesPerTrip();
		final Integer minimumNumberNotesPerTrip = this.noteService.findMinNotesPerTrip();
		final Integer maximumNumberNotesPerTrip = this.noteService.findMaxNotesPerTrip();
		final double standardDeviationOfNumberNotesPerTrip = this.noteService.findStandardDeviationOfNotesPerTrip();
		final double averageAuditsPerTrip = this.auditService.findAverageOfAuditsPerTrip();
		final Integer minimumAuditsPerTrip = this.auditService.findMinAuditRecordsPerTrip();
		final Integer maximumAuditsPerTrip = this.auditService.findMaxAuditsPerTrip();
		final double standardDeviationOfAuditsPerTrip = this.auditService.findStandardDeviationOfAuditsPerTrip();
		final double ratioTripsWithAuditRecord = this.tripService.findRatioOfTripsWithAuditRecord();
		final double ratioRangersWithCurriculaRegistered = this.rangerService.findRatioOfRangersWithCurricula();
		final double ratioRangersWithCurriculumEndorsed = this.rangerService.findRatioOfRangersWithCurriculumEndorsed();
		final double ratioOfSuspiciousManagers = this.managerService.findRatioOfSuspiciousManagers();

		result = new ModelAndView("administrator/dashboard");

		result.addObject("averageApplicationsPerTrip", averageApplicationsPerTrip);
		result.addObject("minimumApplicationsPerTrip", minimumApplicationsPerTrip);
		result.addObject("maximumApplicationsPerTrip", maximumApplicationsPerTrip);
		result.addObject("standardDeviationApplicationsPerTrip", standardDeviationApplicationsPerTrip);

		result.addObject("averageTripsPerManager", averageTripsPerManager);
		result.addObject("minimumTripsPerManager", minimumTripsPerManager);
		result.addObject("maximumTripsPerManager", maximumTripsPerManager);
		result.addObject("standardDeviationOfTripPerManager", standardDeviationOfTripPerManager);

		result.addObject("averagePriceOfTrips", averagePriceOfTrips);
		result.addObject("minimumPriceOfTrips", minimumPriceOfTrips);
		result.addObject("maximumPriceOfTrips", maximumPriceOfTrips);
		result.addObject("standardDeviationPriceOfTrips", standardDeviationPriceOfTrips);
		result.addObject("averageTripsPerRanger", averageTripsPerRanger);
		result.addObject("minimumTripsPerRanger", minimumTripsPerRanger);
		result.addObject("maximumTripsPerRanger", maximumTripsPerRanger);
		result.addObject("standardDeviationTripsPerRanger", standardDeviationTripsPerRanger);
		result.addObject("ratioApplicationsWithPending", ratioApplicationsWithPending);
		result.addObject("ratioApplicationsWithDue", ratioApplicationsWithDue);
		result.addObject("ratioApplicationsWithAccepted", ratioApplicationsWithAccepted);
		result.addObject("ratioApplicationsWithCancelled", ratioApplicationsWithCancelled);
		result.addObject("ratioCancelledTripsVersusTotal", ratioCancelledTripsVersusTotal);
		result.addObject("nameTripsWith10MoreThanAverageOrdered", nameTripsWith10MoreThanAverageOrdered);
		result.addObject("legalTextName", legalTextName);
		result.addObject("averageNumberNotesPerTrip", averageNumberNotesPerTrip);
		result.addObject("minimumNumberNotesPerTrip", minimumNumberNotesPerTrip);
		result.addObject("maximumNumberNotesPerTrip", maximumNumberNotesPerTrip);
		result.addObject("standardDeviationOfNumberNotesPerTrip", standardDeviationOfNumberNotesPerTrip);
		result.addObject("averageAuditsPerTrip", averageAuditsPerTrip);
		result.addObject("minimumAuditsPerTrip", minimumAuditsPerTrip);
		result.addObject("maximumAuditsPerTrip", maximumAuditsPerTrip);
		result.addObject("standardDeviationOfAuditsPerTrip", standardDeviationOfAuditsPerTrip);
		result.addObject("ratioTripsWithAuditRecord", ratioTripsWithAuditRecord);
		result.addObject("ratioRangersWithCurriculaRegistered", ratioRangersWithCurriculaRegistered);
		result.addObject("ratioRangersWithCurriculumEndorsed", ratioRangersWithCurriculumEndorsed);
		result.addObject("ratioOfSuspiciousManagers", ratioOfSuspiciousManagers);
		result.addObject("requestURI", "administrator/dashboard.do");
		return result;
	}
}
