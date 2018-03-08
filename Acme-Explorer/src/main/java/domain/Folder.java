
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Folder extends DomainEntity {

	// Attributes
	private String	name;
	private boolean	systemFolder;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	public boolean getSystemFolder() {
		return this.systemFolder;
	}
	public void setSystemFolder(final boolean systemFolder) {
		this.systemFolder = systemFolder;
	}


	// Relationships
	private Folder	parent;


	@Valid
	@ManyToOne(optional = true)
	public Folder getParent() {
		return this.parent;
	}
	public void setParent(final Folder parent) {
		this.parent = parent;
	}

}
