
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	private double				vatNumber;
	private String				bannerUrl;
	private String				welcomeMessageEnglish;
	private String				welcomeMessageSpanish;
	private String				defaultCC;
	private Integer				defaultFinderNumber;
	private Integer				defaultCacheTime;
	private Collection<String>	spamWords;


	@ElementCollection
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@DecimalMin(value = "0.0")
	public double getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final double vatNumber) {
		this.vatNumber = vatNumber;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@URL
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEnglish() {
		return this.welcomeMessageEnglish;
	}

	public void setWelcomeMessageEnglish(final String welcomeMessageEnglish) {
		this.welcomeMessageEnglish = welcomeMessageEnglish;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageSpanish() {
		return this.welcomeMessageSpanish;
	}

	public void setWelcomeMessageSpanish(final String welcomeMessageSpanish) {
		this.welcomeMessageSpanish = welcomeMessageSpanish;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDefaultCC() {
		return this.defaultCC;
	}

	public void setDefaultCC(final String defaultCC) {
		this.defaultCC = defaultCC;
	}

	@Range(min = 1, max = 100)
	public Integer getDefaultFinderNumber() {
		return this.defaultFinderNumber;
	}

	public void setDefaultFinderNumber(final Integer defaultFinderNumber) {
		this.defaultFinderNumber = defaultFinderNumber;
	}

	@Range(min = 1, max = 24)
	public Integer getDefaultCacheTime() {
		return this.defaultCacheTime;
	}

	public void setDefaultCacheTime(final Integer defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}
}
