
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class QuestionService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private QuestionRepository	questionRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private AnswerService		answerService;


	// Constructors -----------------------------------------------------------

	public QuestionService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Question create(final Rendezvous rendezvous) {
		Question result = null;
		result = new Question();
		List<Answer> answers;
		answers = new ArrayList<Answer>();
		result.setAnswers(answers);
		result.setRendezvous(rendezvous);
		return result;
	}

	public Collection<Question> findAll() {
		Collection<Question> result = null;
		result = this.questionRepository.findAll();

		return result;
	}

	public Question findOne(final int questionId) {
		Question result = null;
		result = this.questionRepository.findOne(questionId);
		return result;
	}

	public Question saveFromCreate(final Question question) {

		Assert.notNull(question, "message.error.question.null");
		Assert.isTrue(question.getId() == 0);
		Assert.notNull(question.getRendezvous());
		Assert.notNull(this.rendezvousService.findOne(question.getRendezvous().getId()));
		Assert.isTrue(this.isCorrectString(question.getText()));
		this.isUserAuthenticate();
		this.isCorrectUser(question.getRendezvous().getId());
		Assert.isTrue(!this.answerIsResponded(question.getRendezvous().getId()), "question.message.error.attendants");
		Question savedQuestion;
		savedQuestion = this.questionRepository.save(question);
		final List<Question> questions;
		questions = new ArrayList<Question>();
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(savedQuestion.getRendezvous().getId());
		questions.addAll(rendezvousInDB.getQuestions());
		questions.add(savedQuestion);
		rendezvousInDB.setQuestions(questions);
		this.rendezvousService.saveWithoutConstraints(rendezvousInDB);
		return savedQuestion;
	}
	public Question saveFromEdit(final Question question) {

		Assert.notNull(question, "message.error.question.null");
		Assert.isTrue(question.getId() != 0);
		Assert.notNull(question.getRendezvous());
		Assert.notNull(this.rendezvousService.findOne(question.getRendezvous().getId()));
		Assert.isTrue(this.isCorrectString(question.getText()));
		this.isCorrectUser(question.getRendezvous().getId());
		Assert.isTrue(this.questionRepository.findOne(question.getId()).getAnswers().isEmpty(), "question.message.error.answers");
		Question savedQuestion;
		savedQuestion = this.questionRepository.save(question);
		final List<Question> questions;
		questions = new ArrayList<Question>();
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(savedQuestion.getRendezvous().getId());
		questions.addAll(rendezvousInDB.getQuestions());

		for (int i = 0; i < questions.size(); i++)
			if (questions.get(i).getId() == savedQuestion.getId()) {
				questions.set(i, savedQuestion);
				break;
			}
		rendezvousInDB.setQuestions(questions);
		this.rendezvousService.saveWithoutConstraints(rendezvousInDB);
		return savedQuestion;
	}
	public Question saveByOtherUser(final Question question) {
		Question savedQuestion;
		savedQuestion = this.questionRepository.save(question);
		final List<Question> questions;
		questions = new ArrayList<Question>();
		Rendezvous rendezvousInDB;
		rendezvousInDB = this.rendezvousService.findOne(savedQuestion.getRendezvous().getId());
		questions.addAll(rendezvousInDB.getQuestions());

		for (int i = 0; i < questions.size(); i++)
			if (questions.get(i).getId() == savedQuestion.getId()) {
				questions.set(i, savedQuestion);
				break;
			}
		rendezvousInDB.setQuestions(questions);
		this.rendezvousService.saveWithoutConstraints(rendezvousInDB);
		return savedQuestion;
	}
	public void delete(final Question question) {
		Question questionInDB;
		questionInDB = this.questionRepository.findOne(question.getId());
		Assert.notNull(questionInDB);
		Assert.isTrue(questionInDB.getAnswers().isEmpty(), "question.message.error.answers");
		this.isCorrectUser(questionInDB.getRendezvous().getId());
		Rendezvous rendezvous;
		rendezvous = questionInDB.getRendezvous();
		rendezvous.getQuestions().remove(questionInDB);
		List<Answer> answers;
		answers = new ArrayList<Answer>(questionInDB.getAnswers());
		for (final Answer a : answers)
			this.answerService.delete(a);
		this.questionRepository.delete(question);
		this.rendezvousService.saveWithoutConstraints(rendezvous);
	}
	public void deleteByAdmin(final Question question) {
		Assert.notNull(question, "message.error.question.null");

		Question questionInDB;
		questionInDB = this.questionRepository.findOne(question.getId());
		Assert.notNull(questionInDB);
		Rendezvous rendezvous;
		rendezvous = questionInDB.getRendezvous();
		rendezvous.getQuestions().remove(questionInDB);
		for (final Answer a : questionInDB.getAnswers())
			this.answerService.delete(a);
		this.questionRepository.delete(question);
		this.rendezvousService.saveWithoutConstraints(rendezvous);

	}
	public void flush() {
		this.questionRepository.flush();
	}
	// Other business methods -------------------------------------------------
	//Auxiliares
	private User isUserAuthenticate() {
		User user;
		user = this.userService.findByPrincipal();
		Assert.notNull(user);
		return user;
	}
	private void isCorrectUser(final int rendezvousId) {

		Assert.isTrue(this.isUserAuthenticate().getRendezvoussesCreated().contains(this.rendezvousService.findOne(rendezvousId)));

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

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 1.0 - Requisito 22.1.1
	public Double findAvgNoQuestionsPerRendezvous() {
		Double result = null;
		result = this.questionRepository.findAvgNoQuestionsPerRendezvous();
		return result;
	}

	public Double findStdNoQuestionsPerRendezvous() {
		Double result = null;
		result = this.questionRepository.findStdNoQuestionsPerRendezvous();
		return result;
	}
	//Auxiliar method
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
