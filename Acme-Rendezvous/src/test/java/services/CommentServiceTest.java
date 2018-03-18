
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Comment;
import domain.Manager;
import domain.RSVP;
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

	@Autowired
	private RSVPService			rsvpService;

	@Autowired
	private AdministratorService			administratorService;
	//Begin tests

	@Test
	public void testDeleteComment() {
		// Comment: text, optional URL picture,rendezvous, expectedException
		Comment comment = this.commentService.findOne(this.getEntityId("comment1"));
					
		final Object[][] testingData = {
				
				{
				comment,null
			},{
				null,NullPointerException.class
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

				this.unauthenticate();
				this.commentService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				messageError = oops.getMessage();
			}

			this.checkExceptionsWithMessage(expectedException, caught, messageError);

		}
	
	
	@Test
	public void testSaveFromCreateComment() {
		// Comment: text, optional URL picture,rendezvous, expectedException
		Rendezvous rendezvous = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		User user = this.userService.findOne(this.getEntityId("user1"));
		//Comment comment = this.commentService.findOne(this.getEntityId("comment3"));
		
		final Object[][] testingData = {
				
				{
				"testText1", null, rendezvous, user,null, null
			},{
				"testText2","http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg",rendezvous, user, null, null
			},{
				null,"http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg",rendezvous, user, null, IllegalArgumentException.class
			},			{
				"testText4", "Lalala", rendezvous, user, null, ConstraintViolationException.class
			}, {
				null, null, null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (Rendezvous) testingData[i][2], (User) testingData[i][3],(Comment) testingData[i][4], (Class<?>) testingData[i][5]);

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
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}

	@Test
	public void testSaveReplyComment() {
		// Comment: text, optional URL picture,rendezvous, expectedException
		Rendezvous rendezvous = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		User user = this.userService.findOne(this.getEntityId("user2"));
		Comment comment = this.commentService.findOne(this.getEntityId("comment2"));
		
		final Object[][] testingData = {
				
				{
				"testText1", null, rendezvous, user,comment, null
			},{
				"testText2","http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg",rendezvous, user, comment, null
			},{
				null,"http://images.nationalgeographic.com.es/medio/2015/12/21/bf63ef82rio_narcea_tineo_720x480.jpg",rendezvous, user, comment, IllegalArgumentException.class
			},			{
				"testText4", "Lalala", rendezvous, user, comment, ConstraintViolationException.class
			}, {
				null, null, null, null, comment, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (Rendezvous) testingData[i][2], (User) testingData[i][3],(Comment) testingData[i][4], (Class<?>) testingData[i][5]);

	}
	
	protected void testSaveReplyTemplate(final String text, final String url, final Rendezvous rendezvous, final User user, final Comment originalComment, final Class<?> expectedException) {

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
			
			this.commentService.saveReply(originalComment, comment);
			
			this.unauthenticate();
			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
	
}
