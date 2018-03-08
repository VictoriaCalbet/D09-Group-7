
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import services.UserService;
import services.form.CommentFormService;
import controllers.AbstractController;
import domain.Comment;
import domain.Rendezvous;
import domain.User;
import domain.form.CommentForm;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	//Constructor

	public CommentUserController() {

		super();
	}


	//Supporting services
	@Autowired
	private CommentService		commentService;

	@Autowired
	private CommentFormService	commentFormService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Creation ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int rendezvousId) {
		ModelAndView result;
		CommentForm commentForm;

		try {
			final User user = this.userService.findByPrincipal();
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);

			Assert.notNull(rendezvous, "message.error.rendezvous.null");

			Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();
			principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(user.getId());
			Assert.isTrue(principalRendezvouses.contains(rendezvous), "message.error.comment.noRSVP");

			commentForm = this.commentFormService.create();
			result = this.createModelAndView(commentForm);
		} catch (final Throwable oops) {
			String messageError = "comment.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/rendezvous/list.do");
			result.addObject("message", messageError);

		}
		return result;

	}

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(final int commentId) {
		ModelAndView result;
		CommentForm commentForm;

		try {
			final User user = this.userService.findByPrincipal();

			Assert.notNull(this.commentService.findOne(commentId), "message.error.comment.badId");

			final Comment comment = this.commentService.findOne(commentId);

			Assert.notNull(this.rendezvousService.findOne(comment.getRendezvous().getId()), "message.error.rendezvous.null");

			final Rendezvous rendezvous = this.rendezvousService.findOne(comment.getRendezvous().getId());

			Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();
			principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(user.getId());
			Assert.isTrue(principalRendezvouses.contains(rendezvous), "message.error.comment.noRSVP");

			commentForm = this.commentFormService.create();
			result = this.createModelAndView(commentForm);
		} catch (final Throwable oops) {
			String messageError = "comment.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();

			result = new ModelAndView("redirect:/rendezvous/list.do");
			result.addObject("message", messageError);

		}
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("commentForm") @Valid final CommentForm commentForm, final BindingResult binding, @RequestParam(required = true) final int rendezvousId) {
		//		this.rendezvousFormValidator.validate(rendezvousForm, binding);

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(commentForm);
		else
			try {
				final User user = this.userService.findByPrincipal();

				Assert.notNull(this.rendezvousService.findOne(rendezvousId), "message.error.rendezvous.null");
				final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
				Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();
				principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(user.getId());
				Assert.isTrue(principalRendezvouses.contains(rendezvous), "message.error.comment.noRSVP");
				this.commentFormService.saveFromCreate(commentForm, rendezvous);
				result = new ModelAndView("redirect:/rendezvous/list.do");

			} catch (final Throwable oops) {
				String messageError = "comment.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				//result = this.createModelAndView(commentForm, messageError);
				//					result = new ModelAndView("rendezvous/list");
				result = new ModelAndView("redirect:/rendezvous/list.do");
				//					Actor actor = this.actorService.findByPrincipal();
				//					final Collection<Rendezvous> rendezvousesError = this.rendezvousService.findRendezvousesLogged(actor);
				//					result.addObject("rendezvouses", rendezvousesError);
				//					result.addObject("requestURI", "rendezvous/list.do");
				result.addObject("message", messageError);

			}

		return result;
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST, params = "save")
	public ModelAndView reply(@ModelAttribute("commentForm") @Valid final CommentForm commentForm, final BindingResult binding, @RequestParam(required = true) final int commentId) {
		//		this.rendezvousFormValidator.validate(rendezvousForm, binding);

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(commentForm);
		else
			try {

				//Assert.notNull(this.commentService.findOne(commentForm.getId()),"message.error.comment.badId");
				Assert.notNull(this.commentService.findOne(commentId), "message.error.comment.badId");
				final Comment comment = this.commentService.findOne(commentId);
				final User user = this.userService.findByPrincipal();
				Collection<Rendezvous> principalRendezvouses = new ArrayList<Rendezvous>();

				principalRendezvouses = this.rendezvousService.findAllAttendedByUserId(user.getId());
				Assert.isTrue(principalRendezvouses.contains(comment.getRendezvous()), "message.error.comment.noRSVP");
				this.commentFormService.saveReply(commentForm, comment);
				result = new ModelAndView("redirect:/rendezvous/list.do");
			} catch (final Throwable oops) {
				String messageError = "comment.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createModelAndView(commentForm, messageError);
			}

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createModelAndView(final CommentForm commentForm) {
		ModelAndView result;

		result = this.createModelAndView(commentForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final CommentForm commentForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("comment/user/create");
		result.addObject("commentForm", commentForm);
		result.addObject("message", message);

		return result;
	}

}
