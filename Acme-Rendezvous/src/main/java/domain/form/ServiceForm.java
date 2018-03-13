
package domain.form;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import domain.Category;

public class ServiceForm {

	// Form attributes --------------------------------------------------------
	private int						id;
	private String					name;
	private String					description;
	private String					pictureURL;
	private Collection<Category>	categories;


	// Methods

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

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

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Category> getCategories() {
		return this.categories;
	}
	public void setCategories(final Collection<Category> categories) {
		this.categories = categories;
	}

}
