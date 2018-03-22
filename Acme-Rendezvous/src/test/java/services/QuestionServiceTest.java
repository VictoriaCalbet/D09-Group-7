
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	//The SUT ----------------------------------------
	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezvousService	rendezvousService;


	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 18
	 * 
	 * The creator of a rendezvous may associate a number of questions with it, each of which
	 * must be answered when a user RSVPs that rendezvous.
	 * 
	 * Positive test 1: Create question by correct user.
	 * Negative test 2: Create question by incorrect user.
	 */
	@Test
	public void testSaveFromCreateQuestion() {

		final Object[][] testingData = {
			{
				//Create by correct user.
				"user3", null
			}, {
				//Create by incorrect user.
				"user1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateQuestionTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void testSaveFromCreateQuestionTemplate(final String user, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate(user);
			Question question;
			question = this.questionService.create(this.rendezvousService.findOne(this.getEntityId("rendezvous6")));
			question.setText("question text1");
			this.questionService.saveFromCreate(question);
			this.questionService.flush();

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
	 * Acme-Rendezvous 1.0: Requirement 21.1
	 * 
	 * Manage the questions that are associated with a rendezvous that he or she’s created
	 * previously.
	 * 
	 * Positive test 1: Edit question by correct user.
	 * Negative test 2: Edit question by incorrect user.
	 */

	@Test
	public void testSaveFromEditQuestion() {

		final Object[][] testingData = {
			{
				//Edit by correct user.
				"user3", null
			}, {
				//Edit by incorrect user.
				"user2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromEditQuestionTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	protected void testSaveFromEditQuestionTemplate(final String user, final Class<?> expectedException) {

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
			questionInDB.setText("question text1");
			questionInDB = this.questionService.saveFromEdit(questionInDB);
			this.questionService.flush();

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
	 * Acme-Rendezvous 1.0: Requirement 21.1
	 * 
	 * Manage the questions that are associated with a rendezvous that he or she’s created
	 * previously.
	 * 
	 * Positive test 1: Delete question by correct user.
	 * Negative test 2: Delete question by incorrect user.
	 */
	@Test
	public void testDeleteQuestion() {

		final Object[][] testingData = {
			{
				//Delete by correct user.
				"user3", null
			}, {
				//Delete by incorrect user.
				"user2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteQuestionTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	protected void testDeleteQuestionTemplate(final String user, final Class<?> expectedException) {

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
			this.questionService.delete(questionInDB);
			this.questionService.flush();

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
	 * Acme-Rendezvous 1.0: Requirement 21.1
	 * 
	 * Manage the questions that are associated with a rendezvous that he or she’s created
	 * previously.
	 * 
	 * Positive test 1: List questions.
	 * Note: this requirements has been development on controllers.
	 */
	@Test
	public void testListQuestion() {

		final Object[][] testingData = {
			{
				//List one question.
				"user3", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteQuestionTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	protected void testListQuestionTemplate(final String user, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate(user);
			Question question;
			question = this.questionService.create(this.rendezvousService.findOne(this.getEntityId("rendezvous6")));
			question.setText("question text1");
			this.questionService.saveFromCreate(question);
			this.questionService.flush();
			Rendezvous rendezvousInDB;
			rendezvousInDB = this.rendezvousService.findOne(this.getEntityId("rendezvous6"));
			Assert.isTrue(rendezvousInDB.getQuestions().size() == 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
}
