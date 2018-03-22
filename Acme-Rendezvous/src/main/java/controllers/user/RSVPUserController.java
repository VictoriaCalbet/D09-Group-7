
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.RSVPService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.RSVP;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/RSVP/user")
public class RSVPUserController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private RSVPService			RSVPService;

	@Autowired
	private UserService			userService;

	@Autowired
	private AnswerService		answerService;


	// Constructors -----------------------------------------------------------

	public RSVPUserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String message) {
		final ModelAndView result;

		final User u = this.userService.findByPrincipal();
		//Parece de poco sentido tener dos colecciones iguales, pero no. La vista de listar
		//necesita dos colecciones de rendezvouses para ser mostrada correctamente. Dependiendo
		//del controlador, la colección rendezvouses cambiará. Lo que no cambiará sera la
		//de principalRendezvouses, que es la que se compara con cada rendezvous de la lista
		//con la funcionalidad de que si ya la contiene, significa que el usuario ya ha 
		//hecho la reserva y no puede volver a reservar.
		Collection<Rendezvous> rendezvouses = new ArrayList<Rendezvous>();
		Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();

		principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(u.getId());
		rendezvouses = principalRendezvouses;
		result = new ModelAndView("rendezvous/list");
		result.addObject("principalRendezvouses", principalRendezvouses);
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("message", message);
		result.addObject("requestURI", "rendezvous/user/list.do");

		return result;
	}

	@RequestMapping(value = "/listRSVPs", method = RequestMethod.GET)
	public ModelAndView listRSVPs(@RequestParam(required = false) final String message) {
		final ModelAndView result;

		final User principal = this.userService.findByPrincipal();
		final Collection<RSVP> rsvps = principal.getRsvps();

		result = new ModelAndView("RSVP/user/listRSVPs");
		result.addObject("principal", principal);
		result.addObject("rsvps", rsvps);
		result.addObject("message", message);
		result.addObject("requestURI", "RSVP/user/listRSVPs.do");

		return result;
	}

	@RequestMapping(value = "/RSVPAssure", method = RequestMethod.GET)
	public ModelAndView rsvpAssure(@RequestParam final int rendezvousId) {

		final ModelAndView result;
		User user;
		user = null;
		try {
			user = this.userService.findByPrincipal();
		} catch (final Throwable oops) {

		}
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
		List<Question> questions;
		questions = new ArrayList<Question>();
		questions.addAll(rendezvousInDB.getQuestions());
		List<Answer> answersInDB;
		answersInDB = new ArrayList<Answer>();
		for (final Question q : questions) {
			Answer answer;
			answer = null;
			if (user != null)
				answer = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), user.getId());
			if (answer != null)
				answersInDB.add(answer);
		}
		if (questions.size() == answersInDB.size()) {
			Rendezvous rv;

			rv = this.rendezvousService.findOne(rendezvousId);
			Assert.notNull(rv, "message.error.rsvp.null");

			result = this.createEditModelAndView(rv);

			result.addObject(rendezvousId);
		} else
			result = new ModelAndView("redirect:/");
		return result;

	}
	@SuppressWarnings("unused")
	@RequestMapping(value = "/RSVP", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int rendezvousId) {
		ModelAndView result;
		User user;
		user = null;
		try {
			user = this.userService.findByPrincipal();
		} catch (final Throwable oops) {

		}
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
		Rendezvous rv;
		rv = rendezvousInDB;
		List<Question> questions;
		questions = new ArrayList<Question>();
		questions.addAll(rendezvousInDB.getQuestions());
		List<Answer> answersInDB;
		answersInDB = new ArrayList<Answer>();
		for (final Question q : questions) {
			Answer answer;
			answer = null;
			if (user != null)
				answer = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), user.getId());
			if (answer != null)
				answersInDB.add(answer);
		}
		if (questions.size() == answersInDB.size()) {

			if (false)//binding.hasErrors())
				result = this.createEditModelAndView(rv);
			else
				try {

					this.RSVPService.RSVPaRendezvous(rv.getId());

					result = new ModelAndView("redirect:/rendezvous/list.do");

				} catch (final Throwable oops) {

					String messageError = "rendezvous.commit.error";

					if (oops.getMessage().contains("message.error"))

						messageError = oops.getMessage();

					result = this.createEditModelAndView(rv, messageError);

				}
		} else
			result = new ModelAndView("redirect:/");
		return result;

	}

	@RequestMapping(value = "/cancelRSVP", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int rendezvousToCancelId) {
		ModelAndView result;
		try {
			this.RSVPService.cancelRSVP(rendezvousToCancelId);
			result = new ModelAndView("redirect:/RSVP/user/listRSVPs.do");
		} catch (final Throwable oops) {
			String messageError = "RSVP.cancel.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/rendezvous/list.do");
			result.addObject("message", messageError);
		}

		final User principal = this.userService.findByPrincipal();
		RSVP rsvpToCancel = null;
		final Collection<RSVP> principalRSVPs = principal.getRsvps();
		for (final RSVP rsvp : principalRSVPs)
			if (rsvp.getRendezvous() == this.rendezvousService.findOne(rendezvousToCancelId))
				rsvpToCancel = rsvp;

		result.addObject("rsvpToCancel", rsvpToCancel);

		result.addObject("rendezvousToCancelId", rendezvousToCancelId);
		return result;
	}
	// Ancillaty methods

	@RequestMapping(value = "/unCancelRSVP", method = RequestMethod.GET)
	public ModelAndView unCancel(@RequestParam final int rendezvousToUnCancelId) {
		ModelAndView result;
		try {
			this.RSVPService.unCancelRSVP(rendezvousToUnCancelId);
			result = new ModelAndView("redirect:/RSVP/user/listRSVPs.do");
		} catch (final Throwable oops) {
			String messageError = "RSVP.cancel.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/rendezvous/list.do");
			result.addObject("message", messageError);
		}

		final User principal = this.userService.findByPrincipal();
		RSVP rsvpToCancel = null;
		final Collection<RSVP> principalRSVPs = principal.getRsvps();
		for (final RSVP rsvp : principalRSVPs)
			if (rsvp.getRendezvous() == this.rendezvousService.findOne(rendezvousToUnCancelId))
				rsvpToCancel = rsvp;

		result.addObject("rsvpToCancel", rsvpToCancel);

		result.addObject("rendezvousToCancelId", rendezvousToUnCancelId);
		return result;
	}

	protected final ModelAndView createEditModelAndView(final Rendezvous rv) {

		final ModelAndView result;

		result = this.createEditModelAndView(rv, null);

		return result;

	}

	protected final ModelAndView createEditModelAndView(final Rendezvous rendezvous, final String messageCode) {

		ModelAndView result;

		result = new ModelAndView("RSVP/user/RSVP");

		result.addObject("rendezvous", rendezvous);

		result.addObject("message", messageCode);

		result.addObject("requestURI", "RSVP/user/RSVP.do");

		return result;

	}

	//Ancillary methods

	protected ModelAndView enrolModelAndView(final Rendezvous rendezvous) {

		ModelAndView result;

		result = this.enrolModelAndView(rendezvous, null);

		return result;

	}

	protected ModelAndView enrolModelAndView(final Rendezvous rendezvous, final String message) {

		ModelAndView result;

		result = new ModelAndView("RSVP/user/RSVP");

		result.addObject("rendezvous", rendezvous);

		result.addObject("message", message);

		return result;

	}

}
