
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Request extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private CreditCard	creditCard;
	private String		comments;


	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComments() {
		return this.comments;
	}
	public void setComments(final String comments) {
		this.comments = comments;
	}


	// Relationships ----------------------------------------------------------

	private Rendezvous	rendezvous;
	private Service		service;


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
	@NotNull
	@ManyToOne(optional = false)
	public Service getService() {
		return this.service;
	}
	public void setService(final Service service) {
		this.service = service;
	}

}
