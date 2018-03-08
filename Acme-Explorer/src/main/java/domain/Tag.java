
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Tag extends DomainEntity {

	// Attributes
	private String				name;
	private Collection<String>	groupOfValues;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Collection<String> getGroupOfValues() {
		return this.groupOfValues;
	}
	public void setGroupOfValues(final Collection<String> groupOfValues) {
		this.groupOfValues = groupOfValues;
	}


	// Relationships

	private Collection<TagValue>	tagValues;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "tag")
	public Collection<TagValue> getTagValues() {
		return this.tagValues;
	}
	public void setTagValues(final Collection<TagValue> tagValues) {
		this.tagValues = tagValues;
	}

}
