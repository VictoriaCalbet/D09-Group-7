
package services;

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
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AnswerServiceTest extends AbstractTest {

	@Autowired
	private UserService		userService;

	@Autowired
	private AnswerService	answerService;


	@Test
	public void testCreate() {
		Answer answer;
		answer = this.answerService.create();
		Assert.isNull(answer.getQuestion());
		Assert.isNull(answer.getUser());
		Assert.isNull(answer.getText());
	}

	@Test
	public void testSavedFromCreate() {
		this.authenticate("user1");
		Answer answer;
		answer = this.answerService.create();
		User userInDB;
		userInDB = null;
		Question questionInDB;
		questionInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1")) {
				userInDB = u;
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1"))
						for (final Question q : r.getQuestions())
							if (q.getText().equals("Question 1 of rendezvous1")) {
								questionInDB = q;
								break;
							}
			}
		if (questionInDB != null && userInDB != null) {
			answer.setQuestion(questionInDB);
			answer.setUser(userInDB);
			answer.setText("Yes");
			Answer savedAnswer;
			savedAnswer = this.answerService.saveFromCreate(answer);
			Answer answerInDB;
			answerInDB = this.answerService.findOne(savedAnswer.getId());
			Assert.notNull(answerInDB);
			Assert.notNull(answerInDB.getText());
			Assert.isTrue(answerInDB.getText().equals("Yes"));
		}
		this.unauthenticate();
	}
	@Test
	public void testSavedFromEdit() {
		this.authenticate("user1");
		Answer answer;
		answer = this.answerService.create();
		User userInDB;
		userInDB = null;
		Question questionInDB;
		questionInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1")) {
				userInDB = u;
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1"))
						for (final Question q : r.getQuestions())
							if (q.getText().equals("Question 1 of rendezvous1")) {
								questionInDB = q;
								break;
							}
			}
		if (questionInDB != null && userInDB != null) {
			answer.setQuestion(questionInDB);
			answer.setUser(userInDB);
			answer.setText("Yes");
			Answer savedAnswer;
			savedAnswer = this.answerService.saveFromCreate(answer);
			Answer answerInDB;
			answerInDB = this.answerService.findOne(savedAnswer.getId());
			Assert.notNull(answerInDB);
			Assert.notNull(answerInDB.getText());
			Assert.isTrue(answerInDB.getText().equals("Yes"));
			answerInDB.setText("No");
			this.answerService.saveFromEdit(answerInDB);
			Answer answerInDB2;
			answerInDB2 = this.answerService.findOne(answerInDB.getId());
			Assert.isTrue(answerInDB2.getText().equals("No"));
		}
		this.unauthenticate();
	}

	@Test
	public void testDelete() {
		this.authenticate("user1");
		Answer answer;
		answer = this.answerService.create();
		User userInDB;
		userInDB = null;
		Question questionInDB;
		questionInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1")) {
				userInDB = u;
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1"))
						for (final Question q : r.getQuestions())
							if (q.getText().equals("Question 1 of rendezvous1")) {
								questionInDB = q;
								break;
							}
			}
		if (questionInDB != null && userInDB != null) {
			answer.setQuestion(questionInDB);
			answer.setUser(userInDB);
			answer.setText("Yes");
			Answer savedAnswer;
			savedAnswer = this.answerService.saveFromCreate(answer);
			Answer answerInDB;
			answerInDB = this.answerService.findOne(savedAnswer.getId());
			Assert.notNull(answerInDB);
			Assert.notNull(answerInDB.getText());
			this.answerService.delete(answerInDB);
			Assert.isNull(this.answerService.findOne(answerInDB.getId()));

		}
		this.unauthenticate();
	}

	@Test
	public void testfindAnswerByQuestionIdAndUserId() {
		this.authenticate("user1");
		Answer answer;
		answer = this.answerService.create();
		User userInDB;
		userInDB = null;
		Question questionInDB;
		questionInDB = null;
		for (final User u : this.userService.findAll())
			if (u.getUserAccount().getUsername().equals("user1")) {
				userInDB = u;
				for (final Rendezvous r : u.getRendezvoussesCreated())
					if (r.getName().equals("This is rendezvous1"))
						for (final Question q : r.getQuestions())
							if (q.getText().equals("Question 1 of rendezvous1")) {
								questionInDB = q;
								break;
							}
			}
		if (questionInDB != null && userInDB != null) {
			this.answerService.delete(this.answerService.findAnswerByQuestionIdAndUserId(questionInDB.getId(), userInDB.getId()));
			answer.setQuestion(questionInDB);
			answer.setUser(userInDB);
			answer.setText("Yes");
			Answer savedAnswer;
			savedAnswer = this.answerService.saveFromCreate(answer);
			Answer answerInDB;
			answerInDB = this.answerService.findOne(savedAnswer.getId());
			Assert.notNull(answerInDB);
			Assert.notNull(answerInDB.getText());
			Answer answerInDB2;
			answerInDB2 = this.answerService.findAnswerByQuestionIdAndUserId(questionInDB.getId(), userInDB.getId());
			Assert.isTrue(answerInDB2.equals(answerInDB));
		}
		this.unauthenticate();
	}
}
