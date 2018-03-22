
package services.form;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import services.AdministratorService;
import services.CommentService;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.form.CommentForm;

@Service
@Transactional
public class CommentFormService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private CommentRepository		commentRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CommentService			commentService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public CommentFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public CommentForm create() {
		CommentForm result = null;
		result = new CommentForm();
		result.setMomentWritten(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	//Create CommentForm from comment

	public CommentForm create(final int commentId) {

		CommentForm result = null;

		final Comment cO = this.commentRepository.findOne(commentId);
		Assert.notNull(cO, "message.error.comment.null");
		Assert.notNull(cO.getText(), "message.error.comment.text");
		Assert.notNull(cO.getMomentWritten(), "message.error.comment.momentWritten");

		result = new CommentForm();
		result.setId(cO.getId());
		result.setText(cO.getText());
		result.setPicture(cO.getPicture());
		result.setMomentWritten(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	//It is assumed that comments can be posted, but not edited

	public Comment saveFromCreate(final CommentForm cF, final Rendezvous rendez) {

		//Check if the logged actor is a user
		final Comment c = this.commentService.create();

		Assert.isTrue(cF.getId() == 0, "message.error.commentForm.id");
		Assert.notNull(cF, "message.error.comment.null");
		Assert.notNull(cF.getText(), "message.error.comment.text");
		Assert.notNull(cF.getMomentWritten(), "message.error.comment.momentWritten");
		c.setText(cF.getText());
		c.setPicture(cF.getPicture());
		c.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		//		c.setOriginalComment(null);
		//		c.setUser(this.userService.findByPrincipal());
		//		c.setReplies(new ArrayList<Comment>());
		//		c.setRendezvous(rendez);

		final Comment savedC = this.commentService.saveFromCreate(c, rendez);

		//		final User user = this.userService.findByPrincipal();
		//
		//		final Collection<Comment> comments = user.getComments();
		//		comments.add(savedC);
		//		user.setComments(comments);
		//
		//		final Collection<Comment> rendezComments = rendez.getComments();
		//		rendezComments.add(savedC);
		//		rendez.setComments(rendezComments);
		//
		//		this.userService.save(user);
		//		this.rendezvousService.saveWithoutConstraints(rendez);

		return savedC;
	}

	public Comment saveReply(final CommentForm cF, final Comment r) {

		//Check if the logged actor is a user
		//Check if the logged actor is a user
		final Comment c = this.commentService.create();

		Assert.notNull(cF, "message.error.comment.null");
		Assert.notNull(cF.getText(), "message.error.commentForm.text");
		Assert.notNull(cF.getMomentWritten(), "message.error.comment.momentWritten");
		c.setText(cF.getText());
		c.setPicture(cF.getPicture());
		c.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		//		c.setOriginalComment(r);
		//		c.setUser(this.userService.findByPrincipal());
		//		c.setReplies(new ArrayList<Comment>());
		c.setRendezvous(r.getRendezvous());

		final Comment savedC = this.commentService.saveReply(r, c);

		return savedC;
	}

	public void delete(final CommentForm commentF) {

		Assert.notNull(commentF, "message.error.comment.null");
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin, "message.error.comment.notAnAdmin");

		final Comment comment = this.commentRepository.findOne(commentF.getId());

		this.commentService.delete(comment.getId());

		//		final Rendezvous rendez = comment.getRendezvous();
		//		final Collection<Comment> commentsRendez = rendez.getComments();
		//		commentsRendez.remove(comment);
		//		rendez.setComments(commentsRendez);
		//
		//		final Comment commentO = comment.getOriginalComment();
		//
		//		if (commentO != null) {
		//
		//			final Collection<Comment> replies = commentO.getReplies();
		//			replies.remove(comment);
		//			commentO.setReplies(replies);
		//			this.commentRepository.save(commentO);
		//		}
		//
		//		//guardar usuario, rendezvous, borrar replies de comentario original si lo tiene y borrar replies de este comentario en cascada
		//
		//		this.rendezvousService.saveWithoutConstraints(rendez);
		//		final User user = comment.getUser();
		//		user.getComments().remove(comment);
		//		this.userService.save(user);
		//
		//		this.commentRepository.delete(comment);

	}

}
