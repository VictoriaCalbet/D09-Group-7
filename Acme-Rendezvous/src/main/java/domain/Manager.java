
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	// Attributes -------------------------------------------------------------
	private String	VAT;


	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9-]+")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getVAT() {
		return this.VAT;
	}

	public void setVAT(final String vAT) {
		this.VAT = vAT;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Service>	services;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "manager")
	public Collection<Service> getServices() {
		return this.services;
	}

	public void setServices(final Collection<Service> services) {
		this.services = services;
	}
}
