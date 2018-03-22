
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.AnswerService;
import services.CategoryService;
import services.CommentService;
import services.ManagerService;
import services.QuestionService;
import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import controllers.AbstractController;
import domain.Manager;
import domain.Rendezvous;
import domain.Service;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services -------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private UserService			userService;


	// Constructors ---------------------------------------------------------

	public DashboardAdministratorController() {
		super();
	}

	// Dashboard ------------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {
		ModelAndView result = null;

		result = new ModelAndView("administrator/dashboard");

		// Acme-Rendezvous 1.0

		// Requisito 6.3.1
		final Double avgRendezvousesCreatedPerUser = this.rendezvousService.findAvgRendezvousesCreatedPerUser();
		final Double stdRendezvousesCreatedPerUser = this.rendezvousService.findStdRendezvousesCreatedPerUser();
		result.addObject("avgRendezvousesCreatedPerUser", avgRendezvousesCreatedPerUser);
		result.addObject("stdRendezvousesCreatedPerUser", stdRendezvousesCreatedPerUser);

		// Requisito 6.3.2 
		final Double ratioUserRendezvousesCreatedVsNeverCreated = this.userService.findRatioUserRendezvousesCreatedVsNeverCreated();
		result.addObject("ratioUserRendezvousesCreatedVsNeverCreated", ratioUserRendezvousesCreatedVsNeverCreated);

		// Requisito 6.3.3
		final Double avgUsersRSVPsPerRendezvous = this.userService.findAvgUsersRSVPsPerRendezvous();
		final Double stdUsersRSVPsPerRendezvous = this.userService.findStdUsersRSVPsPerRendezvous();
		result.addObject("avgUsersRSVPsPerRendezvous", avgUsersRSVPsPerRendezvous);
		result.addObject("stdUsersRSVPsPerRendezvous", stdUsersRSVPsPerRendezvous);

		// Requisito 6.3.4
		final Double avgRendezvousRSVPsPerUsers = this.rendezvousService.findAvgRendezvousRSVPsPerUsers();
		final Double stdRendezvousRSVPsPerUsers = this.rendezvousService.findStdRendezvousRSVPsPerUsers();
		result.addObject("avgRendezvousRSVPsPerUsers", avgRendezvousRSVPsPerUsers);
		result.addObject("stdRendezvousRSVPsPerUsers", stdRendezvousRSVPsPerUsers);

		// Requisito 6.3.5
		final Collection<Rendezvous> top10RendezvousByRSVPs = this.rendezvousService.findTop10RendezvousByRSVPs();
		result.addObject("top10RendezvousByRSVPs", top10RendezvousByRSVPs);

		// Requisito 17.2.1
		final Double avgAnnouncementPerRendezvous = this.announcementService.findAvgAnnouncementPerRendezvous();
		final Double stdAnnouncementPerRendezvous = this.announcementService.findStdAnnouncementPerRendezvous();
		result.addObject("avgAnnouncementPerRendezvous", avgAnnouncementPerRendezvous);
		result.addObject("stdAnnouncementPerRendezvous", stdAnnouncementPerRendezvous);

		// Requisito 17.2.2
		final Collection<Rendezvous> rendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous = this.rendezvousService.findAllRendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous();
		result.addObject("rendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous", rendezvousNoAnnouncementsIsAbove75PerCentNoAnnouncementPerRendezvous);

		// Requisito 17.2.3
		final Collection<Rendezvous> rendezvousesThatLinkedToRvGreaterThanAvgPlus10 = this.rendezvousService.findRendezvousesThatLinkedToRvGreaterThanAvgPlus10();
		result.addObject("rendezvousesThatLinkedToRvGreaterThanAvgPlus10", rendezvousesThatLinkedToRvGreaterThanAvgPlus10);

		// Requisito 22.1.1
		final Double avgNoQuestionPerRendezvous = this.questionService.findAvgNoQuestionsPerRendezvous();
		final Double stdNoQuestionPerRendezvous = this.questionService.findStdNoQuestionsPerRendezvous();
		result.addObject("avgNoQuestionPerRendezvous", avgNoQuestionPerRendezvous);
		result.addObject("stdNoQuestionPerRendezvous", stdNoQuestionPerRendezvous);

		// Requisito 22.1.2
		final Double avgNoAnswersToTheQuestionsPerRendezvous = this.answerService.findAvgNoAnswersToTheQuestionsPerRendezvous();
		final Double stdNoAnswersToTheQuestionsPerRendezvous = this.answerService.findStdNoAnswersToTheQuestionsPerRendezvous();
		result.addObject("avgNoAnswersToTheQuestionsPerRendezvous", avgNoAnswersToTheQuestionsPerRendezvous);
		result.addObject("stdNoAnswersToTheQuestionsPerRendezvous", stdNoAnswersToTheQuestionsPerRendezvous);

		// Requisito 22.1.3
		final Double avgRepliesPerComment = this.commentService.findAvgRepliesPerComment();
		final Double stdRepliesPerComment = this.commentService.findStdRepliesPerComment();
		result.addObject("avgRepliesPerComment", avgRepliesPerComment);
		result.addObject("stdRepliesPerComment", stdRepliesPerComment);

		// Acme-Rendezvous 2.0

		// Requisito 6.2.1
		final Collection<Service> bestSellingServices = this.serviceService.findBestSellingServices();
		result.addObject("bestSellingServices", bestSellingServices);

		// Requisito 6.2.2
		final Collection<Manager> managersWithMoreServicesThanAverage = this.managerService.findManagersWithMoreServicesThanAverage();
		result.addObject("managersWithMoreServicesThanAverage", managersWithMoreServicesThanAverage);

		// Requisito 6.2.3
		final Collection<Manager> managersWithMoreServicesCancelled = this.managerService.findManagersWithMoreServicesCancelled();
		result.addObject("managersWithMoreServicesCancelled", managersWithMoreServicesCancelled);

		// Requisito 11.2.1
		final Double avgCategoriesCreatedPerRendezvous = this.categoryService.findAvgCategoriesCreatedPerRendezvous();
		result.addObject("avgCategoriesCreatedPerRendezvous", avgCategoriesCreatedPerRendezvous);

		// Requisito 11.2.2
		final Double ratioOfServicesPerEachCategory = this.categoryService.getRatioOfServicesPerEachCategory();
		final Double avgOfServicesPerEachCategory = this.categoryService.getAvgOfServicesPerEachCategory();
		result.addObject("ratioOfServicesPerEachCategory", ratioOfServicesPerEachCategory);
		result.addObject("avgOfServicesPerEachCategory", avgOfServicesPerEachCategory);

		// Requisito 11.2.3
		final Double avgServicesRequestedPerRendezvous = this.serviceService.findAvgServicesRequestedPerRendezvous();
		final Double minServicesRequestedPerRendezvous = this.serviceService.findMinServicesRequestedPerRendezvous();
		final Double maxServicesRequestedPerRendezvous = this.serviceService.findMaxServicesRequestedPerRendezvous();
		final Double stdServicesRequestedPerRendezvous = this.serviceService.findStdServicesRequestedPerRendezvous();
		result.addObject("avgServicesRequestedPerRendezvous", avgServicesRequestedPerRendezvous);
		result.addObject("minServicesRequestedPerRendezvous", minServicesRequestedPerRendezvous);
		result.addObject("maxServicesRequestedPerRendezvous", maxServicesRequestedPerRendezvous);
		result.addObject("stdServicesRequestedPerRendezvous", stdServicesRequestedPerRendezvous);

		// Requisito 11.2.4
		final Collection<Service> topSellingServices = this.serviceService.findTopSellingServices();
		result.addObject("topSellingServices", topSellingServices);

		result.addObject("requestURI", "administrator/dashboard.do");

		return result;
	}

	// Display --------------------------------------------------------------

	// Creation  ------------------------------------------------------------

	// Edition    -----------------------------------------------------------

	// Ancillary methods ----------------------------------------------------
}
