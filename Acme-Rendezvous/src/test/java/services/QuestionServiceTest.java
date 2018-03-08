
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
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	rendezvousService;


	@Test
	public void testCreate() {
		Question question;
		Rendezvous rendezvouSInDB;
		rendezvouSInDB = null;
		for (final Rendezvous r : this.rendezvousService.findAll())
			rendezvouSInDB = r;
		if (rendezvouSInDB != null) {
			question = this.questionService.create(rendezvouSInDB);
			Assert.notNull(question.getAnswers());
			Assert.notNull(question.getRendezvous());
			Assert.isNull(question.getText());
			Assert.isTrue(question.getAnswers().isEmpty());
		}
	}
	@Test
	public void testSavedFromCreate() {
		this.authenticate("user1");
		Question question;
		Rendezvous rendezvousInDB;
		rendezvousInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1"))
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1")) {
						rendezvousInDB = r;
						break;
					}
		if (rendezvousInDB != null) {
			question = this.questionService.create(rendezvousInDB);
			question.setText("What?");
			Question questionSaved;
			questionSaved = this.questionService.saveFromCreate(question);
			Question questionInDB;
			questionInDB = this.questionService.findOne(questionSaved.getId());
			Assert.notNull(questionInDB.getAnswers());
			Assert.isTrue(questionInDB.getAnswers().equals(questionSaved.getAnswers()));

		}
		this.unauthenticate();

	}
	@Test
	public void testSavedFromEdit() {
		this.authenticate("user1");
		Question question;
		Rendezvous rendezvousInDB;
		rendezvousInDB = null;

		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1"))
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1")) {
						rendezvousInDB = r;
						break;
					}

		if (rendezvousInDB != null) {
			question = this.questionService.create(rendezvousInDB);
			question.setText("What?");
			Question questionSaved;
			questionSaved = this.questionService.saveFromCreate(question);
			Question questionInDB;
			questionInDB = this.questionService.findOne(questionSaved.getId());
			Assert.notNull(questionInDB.getAnswers());
			Assert.isTrue(questionInDB.getAnswers().equals(questionSaved.getAnswers()));
			questionInDB.setText("Who?");
			this.questionService.saveFromEdit(questionInDB);
			Question questionInDB2;
			questionInDB2 = this.questionService.findOne(questionInDB.getId());
			Assert.isTrue(questionInDB2.getText().equals("Who?"));
		}
		this.unauthenticate();
	}

	@Test
	public void testSaveByOtherUser() {
		Question question;
		Rendezvous rendezvousInDB;
		rendezvousInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1"))
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1")) {
						rendezvousInDB = r;
						break;
					}
		if (rendezvousInDB != null) {
			question = this.questionService.create(rendezvousInDB);
			question.setText("What?");
			Question questionSaved;
			questionSaved = this.questionService.saveByOtherUser(question);
			Question questionInDB;
			questionInDB = this.questionService.findOne(questionSaved.getId());
			Assert.notNull(questionInDB.getAnswers());
			Assert.isTrue(questionInDB.getAnswers().equals(questionSaved.getAnswers()));

		}
	}

	@Test
	public void testDelete() {
		this.authenticate("user1");
		Question question;
		Rendezvous rendezvousInDB;
		rendezvousInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1"))
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1")) {
						rendezvousInDB = r;
						break;
					}
		if (rendezvousInDB != null) {
			question = this.questionService.create(rendezvousInDB);
			question.setText("What?");
			Question questionSaved;
			questionSaved = this.questionService.saveFromCreate(question);
			Question questionInDB;
			questionInDB = this.questionService.findOne(questionSaved.getId());
			Assert.notNull(questionInDB.getAnswers());
			Assert.isTrue(questionInDB.getAnswers().equals(questionSaved.getAnswers()));
			this.questionService.delete(questionInDB);
			Assert.isNull(this.questionService.findOne(questionInDB.getId()));
		}
		this.unauthenticate();

	}

}
