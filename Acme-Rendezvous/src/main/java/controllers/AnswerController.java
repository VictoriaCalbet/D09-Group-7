
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.RendezvousService;
import services.UserService;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;
import domain.form.QuestionAndAnswerForm;

@Controller
@RequestMapping("/answer")
public class AnswerController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;
	@Autowired
	private AnswerService		answerService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId, @RequestParam final int userId) {
		ModelAndView result;
		try {
			final List<QuestionAndAnswerForm> questionsAndAnswers;
			questionsAndAnswers = new ArrayList<QuestionAndAnswerForm>();
			final List<Question> questions;
			questions = new ArrayList<Question>();
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
			questions.addAll(rendezvousInDB.getQuestions());
			User user;
			user = null;
			try {
				user = this.userService.findOne(userId);
			} catch (final Throwable oops) {

			}
			if (user == null)
				result = new ModelAndView("redirect:/");
			else {
				for (final Question q : questions) {
					QuestionAndAnswerForm qaa;
					qaa = new QuestionAndAnswerForm();
					qaa.setQuestionId(q.getId());
					qaa.setQuestionText(q.getText());

					Answer answer;
					answer = null;
					if (user != null)
						answer = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), user.getId());
					if (answer != null) {
						qaa.setAnswerText(answer.getText());
						questionsAndAnswers.add(qaa);
					}

				}
				result = new ModelAndView("answer/list");
				result.addObject("questionsAndAnswers", questionsAndAnswers);
				result.addObject("requestURI", "answer/user/list.do?rendezvousId=" + rendezvousId);
				final int todoRespondido = 0;
				result.addObject("todoRespondido", todoRespondido);
				result.addObject("respondible", 0);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}
}
