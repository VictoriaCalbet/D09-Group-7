
package controllers;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.RendezvousService;
import services.UserService;
import services.form.ActorFormService;
import domain.Actor;
import domain.Rendezvous;
import domain.User;
import domain.form.ActorForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ActorFormService	actorFormService;

	@Autowired
	private ActorService		actorService;


	public UserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("requestURI", "user/list.do");
		//Needed for answers
		result.addObject("showAnswers", 0);
		result.addObject("rendezvousId", 0);
		result.addObject("isCreatorOfRendezvous", 1);
		return result;
	}

	@RequestMapping(value = "/listAttendant", method = RequestMethod.GET)
	public ModelAndView listAttendant(@RequestParam final int rendezvousId, @RequestParam(required = false) final String message) {
		final ModelAndView result;
		final Collection<User> users = this.userService.findAttendantsOfRendezvous(rendezvousId);

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("message", message);
		result.addObject("requestURI", "user/listAttendant.do");

		//Needed for answers
		int creatorUserId;
		creatorUserId = this.rendezvousService.findOne(rendezvousId).getCreator().getId();

		result.addObject("showAnswers", 1);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("creatorUserId", creatorUserId);
		return result;
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ModelAndView info(@RequestParam final int rendezvousId) {
		ModelAndView result = new ModelAndView();

		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final User user = r.getCreator();

		result = this.infoModelAndView(user);
		return result;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile(@RequestParam final int userId) {
		ModelAndView result = new ModelAndView();

		final User user = this.userService.findOne(userId);
		Assert.notNull(user);

		result = this.infoModelAndView(user);
		return result;
	}

	// Creation ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		ActorForm actorForm;

		actorForm = this.actorFormService.create();
		result = this.createEditModelAndView(actorForm);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				if (actorForm.getId() != 0)
					this.actorFormService.saveFromEdit(actorForm, "USER");
				else
					this.actorFormService.saveFromCreate(actorForm, "USER");
				result = new ModelAndView("redirect:../");
			} catch (final Throwable oops) {
				String messageError = "user.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(actorForm, messageError);
			}

		return result;
	}

	// Ancillaty methods

	public ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("actorForm/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("requestURI", "user/edit.do");

		return result;
	}

	protected ModelAndView infoModelAndView(final User user) {
		ModelAndView result;

		result = this.infoModelAndView(user, null);

		return result;
	}

	protected ModelAndView infoModelAndView(final User user, final String message) {
		ModelAndView result;
		Collection<Rendezvous> rendezvouses;

		try {
			final Actor actor = this.actorService.findByPrincipal();
			final Date birthDate = actor.getBirthDate();
			final Calendar now = Calendar.getInstance();
			now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
			final Date yearLimit = now.getTime();
			if (birthDate.before(yearLimit))
				rendezvouses = this.rendezvousService.findAllAttendedByUserId(user.getId());
			else
				rendezvouses = this.rendezvousService.findAllAttendedByUserIdU18(user.getId());
		} catch (final Throwable oops) {
			rendezvouses = this.rendezvousService.findAllAttendedByUserIdU18(user.getId());
		}

		result = new ModelAndView("user/info");
		result.addObject("user", user);
		result.addObject("message", message);
		result.addObject("rendezvouses", rendezvouses);

		return result;
	}
}
