
package domain.form;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.CreditCard;
import domain.Rendezvous;
import domain.Service;

public class RequestForm {

	// Attributes -------------------------------------------------------------

	private int			id;
	private CreditCard	creditCard;
	private String		comment;
	private Service		service;
	private Rendezvous	rendezvous;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComment() {
		return this.comment;
	}
	public void setComment(final String comment) {
		this.comment = comment;
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

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}
	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}
}
