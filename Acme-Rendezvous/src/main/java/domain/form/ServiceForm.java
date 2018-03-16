
package domain.form;

import java.util.ArrayList;
import java.util.Collection;

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
	private int						noRequests;
	private String					name;
	private String					description;
	private String					pictureURL;
	private boolean					isInappropriate;
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

	public boolean getIsInappropriate() {
		return this.isInappropriate;
	}
	public void setIsInappropriate(final boolean isInappropriate) {
		this.isInappropriate = isInappropriate;
	}

	public int getNoRequests() {
		return this.noRequests;
	}

	public void setNoRequests(final int noRequests) {
		this.noRequests = noRequests;
	}

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	public Collection<Category> getCategories() {
		if (this.categories == null)
			return new ArrayList<Category>();
		else
			return this.categories;
	}
	public void setCategories(final Collection<Category> categories) {
		this.categories = categories;
	}

}
