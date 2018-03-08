
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.Index;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {@Index(columnList="originalComment_id")})
public class Comment extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private Date	momentWritten;
	private String	text;
	private String	picture;


	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMomentWritten() {
		return this.momentWritten;
	}

	public void setMomentWritten(final Date momentWritten) {
		this.momentWritten = momentWritten;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}


	// Relationships ----------------------------------------------------------

	private User				user;
	private Rendezvous			rendezvous;
	private Comment				originalComment;
	private Collection<Comment>	replies;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}

	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

	@Valid
	@ManyToOne(optional = true)
	public Comment getOriginalComment() {
		return this.originalComment;
	}

	public void setOriginalComment(final Comment originalComment) {
		this.originalComment = originalComment;
	}

	//@Valid
	@NotNull
	@OneToMany(mappedBy = "originalComment",cascade = CascadeType.REMOVE)
	public Collection<Comment> getReplies() {
		return this.replies;
	}

	public void setReplies(final Collection<Comment> replies) {
		this.replies = replies;
	}
}
