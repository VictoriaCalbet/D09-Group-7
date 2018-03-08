
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Comment;
import domain.RSVP;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
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


	//Begin tests

	@Test
	public void testCreate() {

		this.authenticate("user1");

		final Comment comment = this.commentService.create();

		Assert.isNull(comment.getOriginalComment());
		Assert.isNull(comment.getPicture());
		Assert.isNull(comment.getRendezvous());
		Assert.isNull(comment.getText());
		Assert.isTrue(comment.getId() <= 0);

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate() {

		this.authenticate("user2");

		//Creation of a rendezvous 

		final User principal = this.userService.findByPrincipal();
		Rendezvous r;
		r = this.rendezvousService.create();

		r.setName("Dolar");
		r.setDescription("descripcion1");
		r.setMeetingMoment(new DateTime().plusDays(10).toDate());
		r.setPicture("http://jsequeiros.com/comprimir-fotografias-e-imagenes-con-microsoft-office-picture-manager.html");
		r.setGpsPoint(null);
		r.setIsAdultOnly(false);
		r.setIsDeleted(false);
		r.setIsDraft(false);

		final User creator = principal;
		r.setCreator(creator);

		final Rendezvous savedR = this.rendezvousService.save(r);

		this.unauthenticate();

		//Creation of the RSVP

		this.authenticate("user1");

		final RSVP createdRSVP = this.rsvpService.create(savedR);
		Assert.notNull(createdRSVP);

		Collection<RSVP> principalRSVPS = principal.getRsvps();
		this.rsvpService.save(createdRSVP);
		principalRSVPS = principal.getRsvps();

		this.rsvpService.RSVPaRendezvous(createdRSVP.getRendezvous().getId());

		Assert.isTrue(!principalRSVPS.contains(createdRSVP));

		//Post a comment 

		final Comment comment = this.commentService.create();

		final User user = this.userService.findByPrincipal();

		Assert.isNull(comment.getOriginalComment());
		Assert.isNull(comment.getPicture());
		Assert.isNull(comment.getRendezvous());
		Assert.isNull(comment.getText());
		Assert.isTrue(comment.getId() <= 0);

		comment.setText("Lalala");
		comment.setUser(user);
		comment.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		comment.setOriginalComment(null);
		comment.setRendezvous(savedR);

		final Comment savedC = this.commentService.save(comment);

		Assert.isTrue(savedC.getId() > 0);

		this.unauthenticate();

	}

	@Test
	public void testSaveReply() {

		this.authenticate("user2");
		final User principal = this.userService.findByPrincipal();
		Rendezvous r;
		r = this.rendezvousService.create();

		r.setName("Dolar");
		r.setDescription("descripcion1");
		r.setMeetingMoment(new DateTime().plusDays(10).toDate());
		r.setPicture("http://jsequeiros.com/comprimir-fotografias-e-imagenes-con-microsoft-office-picture-manager.html");
		r.setGpsPoint(null);
		r.setIsAdultOnly(false);
		r.setIsDeleted(false);
		r.setIsDraft(false);

		final User creator = principal;
		r.setCreator(creator);

		final Rendezvous savedR = this.rendezvousService.save(r);

		this.unauthenticate();

		//Creation of the RSVP

		this.authenticate("user1");

		final RSVP createdRSVP = this.rsvpService.create(savedR);
		Assert.notNull(createdRSVP);

		Collection<RSVP> principalRSVPS = principal.getRsvps();
		this.rsvpService.save(createdRSVP);
		principalRSVPS = principal.getRsvps();

		this.rsvpService.RSVPaRendezvous(createdRSVP.getRendezvous().getId());

		Assert.isTrue(!principalRSVPS.contains(createdRSVP));

		//Post a comment

		final Comment comment = this.commentService.create();

		final User user = this.userService.findByPrincipal();

		Assert.isNull(comment.getOriginalComment());
		Assert.isNull(comment.getPicture());
		Assert.isNull(comment.getRendezvous());
		Assert.isNull(comment.getText());
		Assert.isTrue(comment.getId() <= 0);

		comment.setText("Lalala");
		comment.setUser(user);
		comment.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		comment.setOriginalComment(null);
		comment.setRendezvous(savedR);

		final Comment savedC = this.commentService.save(comment);

		Assert.isTrue(savedC.getId() > 0);

		this.unauthenticate();

		this.authenticate("user2");

		final Comment comment2 = this.commentService.create();

		final User user2 = this.userService.findByPrincipal();

		Assert.isNull(comment2.getOriginalComment());
		Assert.isNull(comment2.getPicture());
		Assert.isNull(comment2.getRendezvous());
		Assert.isNull(comment2.getText());
		Assert.isTrue(comment2.getId() <= 0);

		comment.setRendezvous(r);
		comment2.setText("Lalala2");
		comment2.setUser(user2);
		comment2.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		comment2.setOriginalComment(savedC);

		final Comment savedC2 = this.commentService.save(comment2);

		Assert.isTrue(savedC2.getId() > 0);
		Assert.notNull(savedC2.getOriginalComment());

		this.unauthenticate();

	}

	@Test
	public void testDelete() {

		this.authenticate("user2");

		//Creation of a rendezvous 

		final User principal = this.userService.findByPrincipal();
		Rendezvous r;
		r = this.rendezvousService.create();

		r.setName("Dolar");
		r.setDescription("descripcion1");
		r.setMeetingMoment(new DateTime().plusDays(10).toDate());
		r.setPicture("http://jsequeiros.com/comprimir-fotografias-e-imagenes-con-microsoft-office-picture-manager.html");
		r.setGpsPoint(null);
		r.setIsAdultOnly(false);
		r.setIsDeleted(false);
		r.setIsDraft(false);

		final User creator = principal;
		r.setCreator(creator);

		final Rendezvous savedR = this.rendezvousService.save(r);

		this.unauthenticate();

		//Creation of the RSVP

		this.authenticate("user1");

		final RSVP createdRSVP = this.rsvpService.create(savedR);
		Assert.notNull(createdRSVP);

		Collection<RSVP> principalRSVPS = principal.getRsvps();
		this.rsvpService.save(createdRSVP);
		principalRSVPS = principal.getRsvps();

		this.rsvpService.RSVPaRendezvous(createdRSVP.getRendezvous().getId());

		Assert.isTrue(!principalRSVPS.contains(createdRSVP));

		//Post a comment 

		final Comment comment = this.commentService.create();

		final User user = this.userService.findByPrincipal();

		Assert.isNull(comment.getOriginalComment());
		Assert.isNull(comment.getPicture());
		Assert.isNull(comment.getRendezvous());
		Assert.isNull(comment.getText());
		Assert.isTrue(comment.getId() <= 0);

		comment.setText("Lalala");
		comment.setUser(user);
		comment.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		comment.setOriginalComment(null);
		comment.setRendezvous(savedR);

		final Comment savedC = this.commentService.save(comment);

		Assert.isTrue(savedC.getId() > 0);
		this.unauthenticate();

		this.authenticate("admin");

		//Deleting the comment

		final Collection<Comment> commentsRendez = savedR.getComments();
		commentsRendez.remove(comment);
		savedR.setComments(commentsRendez);

		//guardar usuario, rendezvous, borrar replies de comentario original si lo tiene y borrar replies de este comentario en cascada
		this.rendezvousService.save(savedR);

		final User userC = savedC.getUser();
		userC.getComments().remove(comment);
		this.userService.save(userC);

		this.commentService.delete(savedC.getId());

		Assert.isNull(this.commentService.findOne(savedC.getId()));

		this.unauthenticate();

	}

}
