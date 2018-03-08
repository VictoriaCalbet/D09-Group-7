
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
public class Auditor extends Actor {

	// Relationships
	private Collection<Audit>	audits;
	private Collection<Note>	notes;


	@Valid
	@NotNull
	@OneToMany
	public Collection<Audit> getAudits() {
		return this.audits;
	}
	public void setAudits(final Collection<Audit> audits) {
		this.audits = audits;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<Note> getNotes() {
		return this.notes;
	}
	public void setNotes(final Collection<Note> notes) {
		this.notes = notes;
	}
}
