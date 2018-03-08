
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Explorer extends Actor {

	// Attributes
	private CreditCard	creditCard;


	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	// Relationships
	private Collection<SurvivalClass>		survivalClasses;
	private Collection<Story>				stories;
	private Collection<Application>			applications;
	private Collection<ContactEmergency>	contactEmergencies;


	@Valid
	@NotNull
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "explorers")
	public Collection<SurvivalClass> getSurvivalClasses() {
		return this.survivalClasses;
	}
	public void setSurvivalClasses(final Collection<SurvivalClass> sc) {
		this.survivalClasses = sc;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<Story> getStories() {
		return this.stories;
	}

	public void setStories(final Collection<Story> stories) {
		this.stories = stories;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<ContactEmergency> getContactEmergencies() {
		return this.contactEmergencies;
	}

	public void setContactEmergencies(final Collection<ContactEmergency> contactEmergencies) {
		this.contactEmergencies = contactEmergencies;
	}

}
