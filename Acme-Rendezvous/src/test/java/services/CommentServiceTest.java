
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	//Services under test
	@Autowired
	private UserService			userService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private RendezvousService	rendezvousService;


	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 4.6
	 * 
	 * An actor who is authenticated as a user must be able to:
	 * - Comment on the rendezvouses that he or she has RSVPd.
	 * 
	 * Test 1: Positive case, without a picture URL.
	 * Test 2: Positive case, with a picture URL.
	 * Test 3: Negative case. The text of the comment is null.
	 * Test 4: Negative case. The URL pattern is invalid.
	 * Test 5: Negative case. The rendezvous is null
	 */

	//Begin tests

	@Test
	public void testSaveFromCreateComment() {
		// Comment: text, optional URL picture,rendezvous, user, originalComment, expectedException
		final Rendezvous rendezvous = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final User user = this.userService.findOne(this.getEntityId("user1"));

		final Object[][] testingData = {

			{
				"testText1", null, rendezvous, user, null, null
			}, {
				"testText2", "http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg", rendezvous, user, null, null
			}, {
				null, "http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg", rendezvous, user, null, IllegalArgumentException.class
			}, {
				"testText4", "DefinitelyNotAnURL", rendezvous, user, null, ConstraintViolationException.class
			}, {
				"testText5", "http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg", null, user, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (Rendezvous) testingData[i][2], (User) testingData[i][3], (Comment) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void testSaveFromCreateCommentTemplate(final String text, final String url, final Rendezvous rendezvous, final User user, final Comment originalComment, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("user1");

			final Comment comment = this.commentService.create();

			comment.setPicture(url);
			comment.setUser(user);
			comment.setText(text);
			comment.setRendezvous(rendezvous);
			comment.setOriginalComment(originalComment);

			this.commentService.saveFromCreate(comment, rendezvous);

			this.unauthenticate();
			this.commentService.flush();
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
	 * Acme-Rendezvous 1.0: Requirement 19
	 * 
	 * In addition to writing a comment from scratch, a user may reply to a comment.
	 * 
	 * Therefore, an actor authenticated as a user must be able to write a reply to a comment.
	 * 
	 * Test 1: Positive case, without a picture URL.
	 * Test 2: Positive case, with a picture URL.
	 * Test 3: Negative case. The text of the comment is null.
	 * Test 4: Negative case. The URL pattern is invalid.
	 * Test 5: Negative case. The rendezvous is null
	 */

	@Test
	public void testSaveReplyComment() {
		// Comment: text, optional URL picture,rendezvous, user, originalComment, expectedException
		final Rendezvous rendezvous = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final User user = this.userService.findOne(this.getEntityId("user2"));
		final Comment comment = this.commentService.findOne(this.getEntityId("comment2"));

		final Object[][] testingData = {

			{
				"testText1", null, rendezvous, user, comment, null
			}, {
				"testText2", "http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg", rendezvous, user, comment, null
			}, {
				null, "http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg", rendezvous, user, comment, IllegalArgumentException.class
			}, {
				"testText4", "TotallyNotAValidURL", rendezvous, user, comment, ConstraintViolationException.class
			}, {
				"textTest5", null, null, user, comment, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (Rendezvous) testingData[i][2], (User) testingData[i][3], (Comment) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void testSaveReplyTemplate(final String text, final String url, final Rendezvous rendezvous, final User user, final Comment originalComment, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("user1");

			final Comment comment = this.commentService.create();

			if (url != null)
				comment.setPicture(url);
			comment.setUser(user);
			comment.setText(text);

			this.commentService.saveReply(originalComment, comment);

			this.unauthenticate();
			this.commentService.flush();
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
	 * Acme-Rendezvous 1.0: Requirement 6.1
	 * 
	 * An actor who is authenticated as an administrator must be able to:
	 * -Delete a comment that he or she thinks is inappropriate
	 * 
	 * Test 1: Positive case, without a picture URL.
	 * Test 2: Negative case. The comment to delete is null
	 */

	@Test
	public void testDeleteComment() {
		// Comment, expectedException
		final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));

		final Object[][] testingData = {

			{
				comment, null
			}, {
				null, NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteCommentTemplate((Comment) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void testDeleteCommentTemplate(final Comment comment, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("admin");

			this.commentService.delete(comment.getId());

			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}

}
