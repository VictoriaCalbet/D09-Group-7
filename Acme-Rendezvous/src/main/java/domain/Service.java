
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isInappropriate")
})
public class Service extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	name;
	private String	description;
	private String	pictureURL;
	private boolean	isInappropriate;


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

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPictureURL() {
		return this.pictureURL;
	}
	public void setPictureURL(final String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public boolean getIsInappropriate() {
		return this.isInappropriate;
	}
	public void setIsInappropriate(final boolean isInappropriate) {
		this.isInappropriate = isInappropriate;
	}


	// Relationships ----------------------------------------------------------

	private Manager					manager;
	private Collection<Category>	categories;
	private Collection<Request>		requests;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}
	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Category> getCategories() {
		return this.categories;
	}
	public void setCategories(final Collection<Category> categories) {
		this.categories = categories;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "service")
	public Collection<Request> getRequests() {
		return this.requests;
	}
	public void setRequests(final Collection<Request> requests) {
		this.requests = requests;
	}

}
