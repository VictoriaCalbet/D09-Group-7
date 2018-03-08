
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Story extends DomainEntity {

	private String				title;
	private String				pieceOfText;
	private Collection<String>	attachmentUrls;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPieceOfText() {
		return this.pieceOfText;
	}
	public void setPieceOfText(final String pieceOfText) {
		this.pieceOfText = pieceOfText;
	}

	@ElementCollection
	public Collection<String> getAttachmentUrls() {
		return this.attachmentUrls;
	}
	public void setAttachmentUrls(final Collection<String> attachmentUrls) {
		this.attachmentUrls = attachmentUrls;
	}


	// Relationships
	private Trip	trip;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Trip getTrip() {
		return this.trip;
	}
	public void setTrip(final Trip trip) {
		this.trip = trip;
	}

}
