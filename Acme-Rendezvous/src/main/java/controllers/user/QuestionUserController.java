
package controllers.user;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Question;
import domain.Rendezvous;
import domain.form.QuestionForm;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {
		final ModelAndView result;
		if (this.rendezvousService.findOne(rendezvousId) == null)
			result = new ModelAndView("redirect:/");
		else {
			List<Question> questions;
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
			questions = new ArrayList<Question>(rendezvousInDB.getQuestions());

			result = new ModelAndView("question/list");
			result.addObject("requestURI", "question/user/list.do");
			result.addObject("questions", questions);
			result.addObject("rendezvousId", rendezvousInDB.getId());
			int canEdit = 0;
			try {
				if (this.userService.findByPrincipal().getRendezvoussesCreated().contains(this.rendezvousService.findOne(rendezvousId)))
					canEdit = 1;
			} catch (final Throwable oops) {

			}
			int isRSVP = 0;
			if (this.answerIsResponded(rendezvousId))
				isRSVP = 1;
			result.addObject("canEdit", canEdit);
			result.addObject("isRSVP", isRSVP);
		}
		return result;

	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		if (this.rendezvousService.findOne(rendezvousId) == null)
			result = new ModelAndView("redirect:/");
		else
			try {
				if (!this.userService.findByPrincipal().getRendezvoussesCreated().contains(this.rendezvousService.findOne(rendezvousId)))
					result = new ModelAndView("redirect:/");
				else {
					QuestionForm questionForm;
					questionForm = new QuestionForm();
					questionForm.setRendezvousId(rendezvousId);
					questionForm.setQuestionId(0);
					result = this.createEditModelAndView(questionForm);
					result.addObject("requestURI", "question/user/edit.do");
				}
			} catch (final Throwable oopa) {
				result = new ModelAndView("redirect:/");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int questionId) {
		ModelAndView result;
		Question questionInDB;
		questionInDB = this.questionService.findOne(questionId);
		try {
			if (!this.userService.findByPrincipal().getRendezvoussesCreated().contains(questionInDB.getRendezvous()))
				result = new ModelAndView("redirect:/");
			else {
				QuestionForm questionForm;
				questionForm = new QuestionForm();
				questionForm.setRendezvousId(questionInDB.getRendezvous().getId());
				questionForm.setQuestionId(questionInDB.getId());
				questionForm.setText(questionInDB.getText());
				result = this.createEditModelAndView(questionForm);
				result.addObject("requestURI", "question/user/edit.do");
			}
		} catch (final Throwable oop) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final QuestionForm questionForm, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(questionForm);
		else
			try {
				Question question;

				if (questionForm.getQuestionId() == 0) {
					question = this.questionService.create(this.rendezvousService.findOne(questionForm.getRendezvousId()));
					question.setText(questionForm.getText());
					question.setRendezvous(this.rendezvousService.findOne(questionForm.getRendezvousId()));
					question.setId(questionForm.getQuestionId());
					this.questionService.saveFromCreate(question);
				} else if (questionForm.getQuestionId() != 0) {
					question = this.questionService.findOne(questionForm.getQuestionId());
					question.setText(questionForm.getText());
					this.questionService.saveFromEdit(question);

				}
				result = new ModelAndView("redirect:list.do?rendezvousId=" + questionForm.getRendezvousId());
			} catch (final Throwable oops) {
				String error = "question.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(questionForm, error);
				result.addObject("requestURI", "question/user/edit.do");
			}
		return result;
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final QuestionForm questionForm, final BindingResult binding) {
		ModelAndView result;

		try {
			Question questionInDB;
			questionInDB = this.questionService.findOne(questionForm.getQuestionId());
			this.questionService.delete(questionInDB);
			result = new ModelAndView("redirect:/question/user/list.do?rendezvousId=" + questionForm.getRendezvousId());

		} catch (final Throwable oops) {
			String error = "question.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(questionForm, error);
			result.addObject("requestURI", "question/user/edit.do");
		}

		return result;
	}
	//Auxiliares
	private ModelAndView createEditModelAndView(final QuestionForm questionForm) {
		ModelAndView result;

		result = this.createEditModelAndView(questionForm, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final QuestionForm questionForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("question/edit");
		result.addObject("questionForm", questionForm);
		result.addObject("message", message);

		return result;
	}
	private Boolean answerIsResponded(final int rendezvousId) {
		Boolean answerIsResponded;
		answerIsResponded = false;
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
		//An assert, be careful.
		Assert.notNull(rendezvousInDB);
		List<Question> questions;
		questions = new ArrayList<Question>();
		questions.addAll(rendezvousInDB.getQuestions());

		for (final Question q : questions)
			if (!q.getAnswers().isEmpty()) {
				answerIsResponded = true;
				break;
			}
		if (rendezvousInDB.getRsvps().size() > 1)
			answerIsResponded = true;
		return answerIsResponded;
	}
}
