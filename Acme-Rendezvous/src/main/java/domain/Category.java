
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	name;
	private String	description;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}


	// Relationships ----------------------------------------------------------

	private Category			parent;
	private Collection<Service>	services;


	@Valid
	@OneToOne(optional = true)
	public Category getParent() {
		return this.parent;
	}
	public void setParent(final Category parent) {
		this.parent = parent;
	}

	@Valid
	@NotNull
	@ManyToMany(mappedBy = "categories")
	public Collection<Service> getServices() {
		return this.services;
	}
	public void setServices(final Collection<Service> services) {
		this.services = services;
	}

}
