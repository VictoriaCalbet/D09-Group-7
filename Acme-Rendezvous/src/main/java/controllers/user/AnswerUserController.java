
package controllers.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;
import domain.form.QuestionAndAnswerForm;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController extends AbstractController {

	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;
	@Autowired
	private AnswerService		answerService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {

		final ModelAndView result;
		User user;
		user = null;
		try {
			user = this.userService.findByPrincipal();
		} catch (final Throwable oops) {

		}
		final Date birthDate = user.getBirthDate();
		final Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
		final Date yearLimit = now.getTime();

		if (this.rendezvousService.findOne(rendezvousId).getIsAdultOnly() && birthDate.after(yearLimit))
			result = new ModelAndView("redirect:/");
		else if (this.rendezvousService.findOne(rendezvousId) == null || user == null || user.getRendezvoussesCreated().contains(this.rendezvousService.findOne(rendezvousId)))
			result = new ModelAndView("redirect:/");
		else {
			final List<QuestionAndAnswerForm> questionsAndAnswers;
			questionsAndAnswers = new ArrayList<QuestionAndAnswerForm>();
			final List<Question> questions;
			questions = new ArrayList<Question>();
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
			questions.addAll(rendezvousInDB.getQuestions());
			List<Answer> answersInDB;
			answersInDB = new ArrayList<Answer>();
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
					answersInDB.add(answer);
				} else
					qaa.setAnswerText("");
				questionsAndAnswers.add(qaa);
			}
			result = new ModelAndView("answer/list");
			result.addObject("questionsAndAnswers", questionsAndAnswers);
			result.addObject("requestURI", "answer/user/list.do?rendezvousId=" + rendezvousId);
			int todoRespondido = 0;
			if (questions.size() == answersInDB.size())
				todoRespondido = 1;
			result.addObject("todoRespondido", todoRespondido);
			result.addObject("rendezvousId", rendezvousId);
			result.addObject("respondible", 1);
		}
		return result;
	}

	@RequestMapping(value = "/respond", method = RequestMethod.GET)
	public ModelAndView respond(@RequestParam final int rendezvousId) {

		ModelAndView result;
		User user;
		user = null;
		try {
			user = this.userService.findByPrincipal();
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		final Date birthDate = user.getBirthDate();
		final Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
		final Date yearLimit = now.getTime();

		if (this.rendezvousService.findOne(rendezvousId).getIsAdultOnly() && birthDate.after(yearLimit))
			result = this.listModelAndView("redirect:/rendezvous/user/list.do", "rendezvous.commit.error");
		else if (this.rendezvousService.findOne(rendezvousId) == null || user == null || user.getRendezvoussesCreated().contains(this.rendezvousService.findOne(rendezvousId)))
			result = this.listModelAndView("redirect:/rendezvous/user/list.do", "rendezvous.commit.error");
		else {
			final List<QuestionAndAnswerForm> questionsAndAnswers;
			questionsAndAnswers = new ArrayList<QuestionAndAnswerForm>();
			final List<Question> questions;
			questions = new ArrayList<Question>();
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(rendezvousId);
			questions.addAll(rendezvousInDB.getQuestions());
			if (!questions.isEmpty()) {
				List<Answer> answersInDB;
				answersInDB = new ArrayList<Answer>();
				for (final Question q : questions) {
					QuestionAndAnswerForm qaa;
					qaa = new QuestionAndAnswerForm();
					qaa.setQuestionId(q.getId());
					qaa.setQuestionText(q.getText());
					qaa.setIsBlank(false);
					Answer answer;
					answer = null;
					if (user != null)
						answer = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), user.getId());
					if (answer != null) {
						qaa.setAnswerText(answer.getText());
						qaa.setAnswerId(answer.getId());
						answersInDB.add(answer);
					} else {
						qaa.setAnswerText("");
						qaa.setAnswerId(0);
					}
					questionsAndAnswers.add(qaa);
				}
				result = new ModelAndView("questionsAnswersForm");
				result.addObject("questionsAndAnswers", questionsAndAnswers);
				result.addObject("requestURI", "answer/user/respond.do?rendezvousId=" + rendezvousId);
				result.addObject("rendezvousId", rendezvousId);
			} else
				result = new ModelAndView("redirect:/RSVP/user/RSVP.do?rendezvousId=" + rendezvousId);

		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/respond", method = RequestMethod.POST, params = "save")
	public ModelAndView answerQuestions(final HttpServletRequest request) {
		ModelAndView result = null;
		User user;
		user = null;
		try {
			user = this.userService.findByPrincipal();
		} catch (final Throwable oops) {

		}
		final Date birthDate = user.getBirthDate();
		final Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
		final Date yearLimit = now.getTime();

		if (this.rendezvousService.findOne(new Integer(request.getParameter("rendezvousId"))).getIsAdultOnly() && birthDate.after(yearLimit))
			result = new ModelAndView("redirect:/");
		else if (user == null || user.getRendezvoussesCreated().contains(this.rendezvousService.findOne(new Integer(request.getParameter("rendezvousId")))))
			result = new ModelAndView("redirect:/");
		else {
			List<QuestionAndAnswerForm> questionsAndAnswers;
			questionsAndAnswers = new ArrayList<QuestionAndAnswerForm>();
			try {
				//------------
				Enumeration<String> parameterNames;
				parameterNames = request.getParameterNames();

				List<Answer> answers;
				answers = new ArrayList<Answer>();
				Boolean anyBlank;
				anyBlank = false;
				while (parameterNames.hasMoreElements()) {
					final String name = parameterNames.nextElement();
					if (!(name.equals("save") || name.equals("rendezvousId") || name.contains("answerId"))) {
						final String value = request.getParameter(name);

						Integer questionId;
						questionId = new Integer(name);
						Question questionInDB;
						questionInDB = this.questionService.findOne(questionId);
						Answer answer;
						answer = this.answerService.create();
						answer.setQuestion(questionInDB);
						answer.setText(value);
						answers.add(answer);

						answer.setId(new Integer(request.getParameter("answerId" + name)));
						QuestionAndAnswerForm qaa;
						qaa = new QuestionAndAnswerForm();
						qaa.setAnswerId(0);
						qaa.setAnswerText(value);
						qaa.setQuestionId(questionId);
						qaa.setQuestionText(questionInDB.getText());
						qaa.setIsBlank(this.checkIsBlank(value));
						questionsAndAnswers.add(qaa);
						if (this.checkIsBlank(value))
							anyBlank = true;
					}
				}
				if (anyBlank)
					result = this.createEditModelAndView(questionsAndAnswers, new Integer(request.getParameter("rendezvousId")));
				else {
					for (final Answer a : answers)
						if (a.getId() == 0)
							this.answerService.saveFromCreate(a);
						else {
							a.setUser(user);
							this.answerService.saveFromEdit(a);
						}
					result = new ModelAndView("redirect:/RSVP/user/RSVP.do?rendezvousId=" + request.getParameter("rendezvousId"));
				}
				//---------
			} catch (final Throwable oops) {
				final String messageError = "answer.commit.error";

				result = this.createEditModelAndView(questionsAndAnswers, new Integer(request.getParameter("rendezvousId")), messageError);
			}

		}
		return result;
	}
	/*
	 * @RequestMapping(value = "/meh", method = RequestMethod.GET)
	 * public ModelAndView respond2(@RequestParam final int questionId) {
	 * final ModelAndView result;
	 * User user;
	 * user = null;
	 * try {
	 * user = this.userService.findByPrincipal();
	 * } catch (final Throwable oops) {
	 * 
	 * }
	 * final Date birthDate = user.getBirthDate();
	 * final Calendar now = Calendar.getInstance();
	 * now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
	 * final Date yearLimit = now.getTime();
	 * 
	 * if (this.questionService.findOne(questionId).getRendezvous().getIsAdultOnly() && birthDate.after(yearLimit))
	 * result = new ModelAndView("redirect:/");
	 * else if (this.questionService.findOne(questionId) == null || user == null || user.getRendezvoussesCreated().contains(this.questionService.findOne(questionId).getRendezvous()))
	 * result = new ModelAndView("redirect:/");
	 * else {
	 * QuestionAndAnswerForm questionAndAnswerForm;
	 * questionAndAnswerForm = new QuestionAndAnswerForm();
	 * Question questionInDB;
	 * questionInDB = this.questionService.findOne(questionId);
	 * if (questionInDB != null) {
	 * questionAndAnswerForm.setQuestionId(questionId);
	 * questionAndAnswerForm.setQuestionText(questionInDB.getText());
	 * questionAndAnswerForm.setAnswerText("");
	 * 
	 * Answer answer;
	 * answer = null;
	 * if (user != null)
	 * answer = this.answerService.findAnswerByQuestionIdAndUserId(questionId, user.getId());
	 * if (answer != null)
	 * questionAndAnswerForm.setAnswerId(answer.getId());
	 * else
	 * questionAndAnswerForm.setAnswerId(0);
	 * result = new ModelAndView("answer/edit");
	 * result.addObject("questionAndAnswerForm", questionAndAnswerForm);
	 * result.addObject("requestURI", "answer/user/respond.do?questionId=" + questionId);
	 * result.addObject("rendezvousId", questionInDB.getRendezvous().getId());
	 * } else
	 * result = new ModelAndView("redirect:/");
	 * }
	 * return result;
	 * }
	 * 
	 * @RequestMapping(value = "/meh", method = RequestMethod.POST, params = "save")
	 * public ModelAndView save2(@Valid final QuestionAndAnswerForm questionAndAnswerForm, final BindingResult binding) {
	 * ModelAndView result;
	 * User user;
	 * user = null;
	 * try {
	 * user = this.userService.findByPrincipal();
	 * } catch (final Throwable oops) {
	 * 
	 * }
	 * final Date birthDate = user.getBirthDate();
	 * final Calendar now = Calendar.getInstance();
	 * now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 18);
	 * final Date yearLimit = now.getTime();
	 * 
	 * if (this.questionService.findOne(questionAndAnswerForm.getQuestionId()).getRendezvous().getIsAdultOnly() && birthDate.after(yearLimit))
	 * result = new ModelAndView("redirect:/");
	 * else if (this.questionService.findOne(questionAndAnswerForm.getQuestionId()) == null || user == null || user.getRendezvoussesCreated().contains(this.questionService.findOne(questionAndAnswerForm.getQuestionId()).getRendezvous()))
	 * result = new ModelAndView("redirect:/");
	 * else if (binding.hasErrors())
	 * result = this.createEditModelAndView(questionAndAnswerForm);
	 * else
	 * try {
	 * Question questionInDB;
	 * questionInDB = this.questionService.findOne(questionAndAnswerForm.getQuestionId());
	 * if (questionAndAnswerForm.getAnswerId() == 0) {
	 * Answer answer;
	 * answer = this.answerService.create();
	 * 
	 * answer.setQuestion(questionInDB);
	 * answer.setUser(this.userService.findByPrincipal());
	 * answer.setText(questionAndAnswerForm.getAnswerText());
	 * this.answerService.saveFromCreate(answer);
	 * } else {
	 * Answer answer;
	 * answer = this.answerService.findOne(questionAndAnswerForm.getAnswerId());
	 * answer.setText(questionAndAnswerForm.getAnswerText());
	 * this.answerService.saveFromEdit(answer);
	 * }
	 * 
	 * result = new ModelAndView("redirect:list.do?rendezvousId=" + questionInDB.getRendezvous().getId());
	 * 
	 * } catch (final Throwable oops) {
	 * final String messageError = "answer.commit.error";
	 * 
	 * result = this.createEditModelAndView(questionAndAnswerForm, messageError);
	 * }
	 * return result;
	 * }
	 */
	//Auxialiares
	private ModelAndView createEditModelAndView(final List<QuestionAndAnswerForm> questionAndAnswerForms, final int rendezvousId) {
		ModelAndView result;

		result = this.createEditModelAndView(questionAndAnswerForms, rendezvousId, null);

		return result;
	}
	private ModelAndView createEditModelAndView(final List<QuestionAndAnswerForm> questionAndAnswerForms, final int rendezvousId, final String message) {
		ModelAndView result;
		result = new ModelAndView("questionsAnswersForm");
		result.addObject("questionsAndAnswers", questionAndAnswerForms);
		result.addObject("message", message);
		result.addObject("requestURI", "answer/user/respond.do?rendezvousId=" + rendezvousId);

		return result;
	}
	private Boolean checkIsBlank(final String answer) {
		Boolean isBlank;
		isBlank = false;
		int nSpace;
		nSpace = 0;
		int length;
		length = -1;
		if (answer == null)
			isBlank = true;
		else if (answer.equals(""))
			isBlank = true;
		else {
			length = answer.length();
			for (int i = 0; i < answer.length(); i++)
				if (answer.charAt(i) == ' ')
					nSpace++;
		}
		if (nSpace == (length - 1))
			isBlank = true;
		return isBlank;
	}

	protected ModelAndView listModelAndView(final String string) {
		ModelAndView result;

		result = this.listModelAndView(string, null);

		return result;
	}

	protected ModelAndView listModelAndView(final String string, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("redirect:/rendezvous/user/list.do");
		result.addObject("string", string);
		result.addObject("message", messageCode);
		return result;
	}

}
