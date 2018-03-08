
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;
import domain.Question;
import domain.User;

@Service
@Transactional
public class AnswerService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private AnswerRepository	answerRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService			userService;
	@Autowired
	private QuestionService		questionService;


	// Constructors -----------------------------------------------------------

	public AnswerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Answer create() {
		Answer result = null;
		result = new Answer();

		return result;
	}

	public Collection<Answer> findAll() {
		Collection<Answer> result = null;
		result = this.answerRepository.findAll();
		return result;
	}

	public Answer findOne(final int answerId) {
		Answer result = null;
		result = this.answerRepository.findOne(answerId);
		return result;
	}

	public Answer saveFromCreate(final Answer answer) {
		Assert.notNull(answer, "message.error.answer.null");
		this.isUserAuthenticate();
		Assert.isTrue(answer.getId() == 0);
		answer.setUser(this.userService.findByPrincipal());
		Assert.notNull(this.questionService.findOne(answer.getQuestion().getId()));
		Assert.isTrue(this.isCorrectString(answer.getText()));
		final Answer savedAnswer = this.answerRepository.save(answer);
		Question questionInDB;
		questionInDB = this.questionService.findOne(savedAnswer.getQuestion().getId());
		questionInDB.getAnswers().add(savedAnswer);
		this.questionService.saveByOtherUser(questionInDB);
		return savedAnswer;
	}
	public Answer saveFromEdit(final Answer answer) {
		Assert.notNull(answer, "message.error.answer.null");
		Assert.isTrue(answer.getId() != 0);
		this.isCorrectUser(answer.getUser().getId());

		answer.setUser(this.userService.findByPrincipal());
		Assert.notNull(this.questionService.findOne(answer.getQuestion().getId()));
		Assert.isTrue(answer.getQuestion().getId() == this.answerRepository.findOne(answer.getId()).getQuestion().getId());
		Assert.isTrue(this.isCorrectString(answer.getText()));
		final Answer savedAnswer = this.answerRepository.save(answer);
		Question questionInDB;
		questionInDB = this.questionService.findOne(savedAnswer.getQuestion().getId());
		questionInDB.getAnswers().add(savedAnswer);
		this.questionService.saveByOtherUser(questionInDB);
		return savedAnswer;
	}

	public void delete(final Answer answer) {
		Assert.notNull(answer, "message.error.answer.null");
		Answer answerInDB;
		answerInDB = this.answerRepository.findOne(answer.getId());
		final Question questionInDB = this.questionService.findOne(answer.getQuestion().getId());
		List<Answer> answers;
		answers = new ArrayList<Answer>(questionInDB.getAnswers());
		answers.remove(answerInDB);
		questionInDB.setAnswers(answers);
		this.answerRepository.delete(answerInDB);
		this.questionService.saveFromEdit(questionInDB);

	}
	// Other business methods -------------------------------------------------

	public Answer findAnswerByQuestionIdAndUserId(final int questionId, final int userId) {
		Answer answer;
		answer = this.answerRepository.findAnswerByQuestionIdAndUserId(questionId, userId);
		return answer;
	}
	// Dashboard methods ------------------------------------------------------

	public Double findAvgNoAnswersToTheQuestionsPerRendezvous() {
		Double result = null;
		result = this.answerRepository.findAvgNoAnswersToTheQuestionPerRendezvous();
		return result;
	}

	public Double findStdNoAnswersToTheQuestionsPerRendezvous() {
		Double result = null;
		result = this.answerRepository.findStdNoAnswersToTheQuestionPerRendezvous();
		return result;
	}
	//Auxiliares
	private User isUserAuthenticate() {
		User user;
		user = this.userService.findByPrincipal();
		Assert.notNull(user);
		return user;
	}
	private void isCorrectUser(final int userId) {
		Assert.isTrue(this.isUserAuthenticate().getId() == userId);

	}
	private Boolean isCorrectString(final String string) {
		Boolean correct = true;
		if (string == null || string.equals(""))
			correct = false;
		int blank;
		blank = 0;
		for (int i = 0; i < string.length(); i++)
			if (string.charAt(i) == ' ')
				blank = blank + 1;
		if (blank == string.length())
			correct = false;
		return correct;
	}
}
