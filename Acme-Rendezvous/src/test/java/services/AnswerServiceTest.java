
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.form.QuestionAndAnswerForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnswerServiceTest extends AbstractTest {

	//The SUT ----------------------------------------
	@Autowired
	private AnswerService		answerService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 21.2
	 * 
	 * Answer the questions that are associated with a rendezvous that he or
	 * she’s RSVPing now.
	 * 
	 * Positive test 1: Create answer with not blank text.
	 * Negative test 2: Create answer with blank text
	 */
	@Test
	public void testSaveFromCreateAnswer() {

		final Object[][] testingData = {
			{
				//Create with not blank text
				"text answer", null
			}, {
				//Create with blank text
				"", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateAnswerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void testSaveFromCreateAnswerTemplate(final String answerText, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("user3");
			Question question;
			question = this.questionService.create(this.rendezvousService.findOne(this.getEntityId("rendezvous6")));
			question.setText("question text1");
			Question questionInDB;
			questionInDB = this.questionService.saveFromCreate(question);
			this.questionService.flush();
			this.unauthenticate();
			this.authenticate("user2");
			Answer answer;
			answer = this.answerService.create();
			answer.setText(answerText);
			answer.setQuestion(questionInDB);
			answer.setUser(this.userService.findByPrincipal());
			this.answerService.saveFromCreate(answer);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 20.1
	 * 
	 * Display information about the users who have RSVPd a rendezvous, which, in turn,
	 * must show their answers to the questions that the creator has registered.
	 * 
	 * Positive test 1: List answers.
	 * Note: this requirements has been development on controllers.
	 */
	@Test
	public void testListAnswer() {

		final Object[][] testingData = {
			{

				"user2", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateAnswerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void testListAnswerTemplate(final String user, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("user3");
			Question question;
			question = this.questionService.create(this.rendezvousService.findOne(this.getEntityId("rendezvous6")));
			question.setText("question text1");
			Question questionInDB;
			questionInDB = this.questionService.saveFromCreate(question);
			this.questionService.flush();
			this.unauthenticate();
			this.authenticate(user);
			Answer answer;
			answer = this.answerService.create();
			answer.setText("Answer");
			answer.setQuestion(questionInDB);
			answer.setUser(this.userService.findByPrincipal());
			this.answerService.saveFromCreate(answer);
			final List<Question> questions;
			questions = new ArrayList<Question>();
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(this.getEntityId("rendezvous6"));
			questions.addAll(rendezvousInDB.getQuestions());
			List<Answer> answersInDB;
			answersInDB = new ArrayList<Answer>();
			for (final Question q : questions) {
				QuestionAndAnswerForm qaa;
				qaa = new QuestionAndAnswerForm();
				qaa.setQuestionId(q.getId());
				qaa.setQuestionText(q.getText());

				Answer answer2;
				answer2 = null;
				if (this.userService.findByPrincipal() != null)
					answer2 = this.answerService.findAnswerByQuestionIdAndUserId(q.getId(), this.userService.findByPrincipal().getId());
				if (answer2 != null)
					answersInDB.add(answer2);
			}
			Assert.isTrue(answersInDB.size() == 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}

}
