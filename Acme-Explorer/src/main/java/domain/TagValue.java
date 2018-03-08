
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class TagValue extends DomainEntity {

	// Attributes
	private String	name;
	private String	value;


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
	public String getValue() {
		return this.value;
	}
	public void setValue(final String value) {
		this.value = value;
	}


	// Relationships

	private Trip	trip;
	private Tag		tag;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Trip getTrip() {
		return this.trip;
	}
	public void setTrip(final Trip trip) {
		this.trip = trip;
	}

	@Valid
	// @NotNull is omitted because the Tag can be deleted anytime.
	@ManyToOne(optional = true)
	public Tag getTag() {
		return this.tag;
	}
	public void setTag(final Tag tag) {
		this.tag = tag;
	}
}
