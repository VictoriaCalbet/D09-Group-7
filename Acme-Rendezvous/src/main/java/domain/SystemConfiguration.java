
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	businessName;
	private String	bannerURL;
	private String	englishWelcomeMessage;
	private String	spanishWelcomeMessage;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBusinessName() {
		return this.businessName;
	}
	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	@URL
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBannerURL() {
		return this.bannerURL;
	}
	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getEnglishWelcomeMessage() {
		return this.englishWelcomeMessage;
	}
	public void setEnglishWelcomeMessage(final String englishWelcomeMessage) {
		this.englishWelcomeMessage = englishWelcomeMessage;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSpanishWelcomeMessage() {
		return this.spanishWelcomeMessage;
	}
	public void setSpanishWelcomeMessage(final String spanishWelcomeMessage) {
		this.spanishWelcomeMessage = spanishWelcomeMessage;
	}

	// Relationships ----------------------------------------------------------

}
