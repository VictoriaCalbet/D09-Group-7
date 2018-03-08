
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------

	private Collection<Answer>		answers;
	private Collection<Rendezvous>	rendezvoussesCreated;
	private Collection<RSVP>		rsvps;
	private Collection<Comment>		comments;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "creator")
	public Collection<Rendezvous> getRendezvoussesCreated() {
		return this.rendezvoussesCreated;
	}

	public void setRendezvoussesCreated(final Collection<Rendezvous> rendezvoussesCreated) {
		this.rendezvoussesCreated = rendezvoussesCreated;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<RSVP> getRsvps() {
		return this.rsvps;
	}

	public void setRsvps(final Collection<RSVP> rsvps) {
		this.rsvps = rsvps;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

}
