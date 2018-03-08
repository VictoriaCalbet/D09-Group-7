
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select avg(c.replies.size) from Comment c")
	public int getAverageOfRepliesPerComment();

	@Query("select sqrt(sum(c.replies.size * c.replies.size) / count(c.replies.size) - (avg(c.replies.size) * avg(c.replies.size))) from Comment c")
	public int getStandardDeviationOfRepliesPerComment();

	@Query("select c from Comment c where c.id=?1")
	public Comment getCommentById(int id);

	@Query("select c from Comment c where c.originalComment=null and c.rendezvous.id=?1")
	public Collection<Comment> getOriginalCommentsByRendezvousId(int id);
	
	@Query("select c.replies from Comment c where c.originalComment.id=?1")
	public Collection<Comment> getRepliesOfCommentById(int id);

	// Dashboard queries

	@Query("select avg(cmt.replies.size) from Comment cmt")
	public Double findAvgRepliesPerComment();

	@Query("select sqrt(sum(cmt.replies.size * cmt.replies.size) / count(cmt.replies.size) - (avg(cmt.replies.size) * avg(cmt.replies.size))) from Comment cmt")
	public Double findStdRepliesPerComment();
}
