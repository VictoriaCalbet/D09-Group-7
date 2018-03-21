
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private CommentRepository		commentRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private RendezvousService		rendezvousService;


	// Constructors -----------------------------------------------------------

	public CommentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Comment create() {
		Comment result = null;
		result = new Comment();

		final Collection<Comment> replies = new ArrayList<Comment>();

		result.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		result.setOriginalComment(null);
		result.setUser(this.userService.findByPrincipal());

		result.setReplies(replies);

		return result;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> result = null;
		result = this.commentRepository.findAll();
		return result;
	}

	public Comment findOne(final int commentId) {
		Comment result = null;
		result = this.commentRepository.findOne(commentId);
		return result;
	}

	//It is assumed that comments can be posted, but not edited

	public Comment saveFromCreate(final Comment c, final Rendezvous ren) {

		//Check if the logged actor is a user
		final User user = this.userService.findByPrincipal();

		Assert.notNull(user, "message.error.comment.user");

		Assert.notNull(c, "message.error.comment.null");
		Assert.notNull(c.getText(), "message.error.comment.text");
		Assert.notNull(c.getMomentWritten(), "message.error.comment.momentWritten");
		Assert.notNull(ren, "message.error.comment.rendezvous");
		c.setRendezvous(ren);
		c.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		c.setUser(this.userService.findByPrincipal());
		c.setOriginalComment(null);
		
		final Collection<Comment> comments2 = user.getComments();
		comments2.add(c);
		user.setComments(comments2);
		
		

		final Collection<Comment> comments = ren.getComments();

		comments.add(c);

		ren.setComments(comments);

		this.rendezvousService.saveWithoutConstraints(ren);
		
		this.userService.save(user);
		
		final Comment savedC = this.commentRepository.save(c);

		return savedC;
	}

	public Comment saveReply(final Comment c, final Comment r) {

		//Check if the logged actor is a user
		final User user = this.userService.findByPrincipal();

		Assert.notNull(user, "message.error.comment.user");
		Assert.notNull(r, "message.error.comment.null");
		Assert.notNull(c, "message.error.reply.originalCommentNull");
		Assert.notNull(r, "message.error.comment.null");
		Assert.notNull(r.getText(), "message.error.comment.text");
		Assert.notNull(r.getMomentWritten(), "message.error.comment.momentWritten");
		Assert.notNull(c.getRendezvous(), "message.error.comment.rendezvous");

		r.setRendezvous(c.getRendezvous());
		r.setMomentWritten(new Date(System.currentTimeMillis() - 1));
		r.setUser(this.userService.findByPrincipal());
		r.setOriginalComment(c);

		final Comment savedC = this.commentRepository.save(r);

		final Collection<Comment> replies = c.getReplies();
		replies.add(savedC);
		c.setReplies(replies);
		this.commentRepository.save(c);
		
		final Collection<Comment> comments2 = user.getComments();
		comments2.add(r);
		user.setComments(comments2);
		
		this.userService.save(user);
		
		Rendezvous ren = c.getRendezvous();
		
		final Collection<Comment> comments = ren.getComments();

		comments.add(c);

		ren.setComments(comments);

		this.rendezvousService.saveWithoutConstraints(ren);

		return savedC;
	}

	public Comment save(final Comment c) {

		final Comment savedC = this.commentRepository.save(c);

		return savedC;
	}

	public void delete(final int id) {

		final Comment comment = this.findOne(id);

		Assert.notNull(comment.getId(), "message.error.comment.id");
		Assert.isTrue(comment.getId() > 0, "message.error.comment.id.greaterThan0");
		Assert.notNull(comment, "message.error.comment.null");
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin, "message.error.comment.notAnAdmin");
		Assert.notNull(comment.getMomentWritten(), "message.error.comment.momentWritten");

		final Rendezvous rendez = comment.getRendezvous();
		final Collection<Comment> commentsRendez = rendez.getComments();
		commentsRendez.remove(comment);
		rendez.setComments(commentsRendez);

		final Comment commentO = comment.getOriginalComment();

		if (commentO != null) {

			final Collection<Comment> replies = commentO.getReplies();
			replies.remove(comment);
			commentO.setReplies(replies);
			this.commentRepository.save(commentO);
		}

		//guardar usuario, rendezvous, borrar replies de comentario original si lo tiene y borrar replies de este comentario en cascada
		this.rendezvousService.saveWithoutConstraints(rendez);

		final User user = comment.getUser();
		user.getComments().remove(comment);
		this.userService.save(user);
		//User user = this.userService.findOne(comment.getUser().getId());		

		//user.getComments().remove(comment);
		//user.setComments(comments);

		//this.userService.save(user);
		this.commentRepository.delete(comment);

	}

	public Collection<Comment> getOriginalCommentsByRendezvousId(final int id) {
		Collection<Comment> comments = null;
		comments = this.commentRepository.getOriginalCommentsByRendezvousId(id);
		return comments;
	}

	// Other business methods -------------------------------------------------

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 1.0 - Requisito 22.1.3
	public Double findAvgRepliesPerComment() {
		Double result = null;
		result = this.commentRepository.findAvgRepliesPerComment();
		return result;
	}

	public Double findStdRepliesPerComment() {
		Double result = null;
		result = this.commentRepository.findStdRepliesPerComment();
		return result;
	}
	
	public void flush(){
		
		this.commentRepository.flush();
	}
}
