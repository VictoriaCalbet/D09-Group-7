
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Trip extends DomainEntity {

	// Attributes
	private String	ticker;
	private String	title;
	private String	description;
	private Double	price;
	private String	requirements;
	private Date	publicationDate;
	private Date	startMoment;
	private Date	endMoment;
	private boolean	cancelled;
	private String	reason;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

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
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@DecimalMin(value = "0.0")
	public Double getPrice() {
		return this.price;
	}
	public void setPrice(final Double price) {
		this.price = price;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getRequirements() {
		return this.requirements;
	}
	public void setRequirements(final String requirements) {
		this.requirements = requirements;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getPublicationDate() {
		return this.publicationDate;
	}
	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getStartMoment() {
		return this.startMoment;
	}
	public void setStartMoment(final Date startMoment) {
		this.startMoment = startMoment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getEndMoment() {
		return this.endMoment;
	}
	public void setEndMoment(final Date endMoment) {
		this.endMoment = endMoment;
	}

	public boolean getCancelled() {
		return this.cancelled;
	}
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getReason() {
		return this.reason;
	}
	public void setReason(final String reason) {
		this.reason = reason;
	}


	// Relationships
	private Collection<Stage>			stages;
	private Collection<Sponsorship>		sponsorships;
	private Collection<Note>			notes;
	private Collection<Audit>			audits;
	private Collection<Story>			stories;
	private Collection<Application>		applications;
	private Collection<SurvivalClass>	survivalClasses;
	private Collection<TagValue>		tagValues;
	private Manager						manager;
	private Ranger						ranger;
	private LegalText					legalText;
	private Category					category;


	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
	public Collection<Stage> getStages() {
		return this.stages;
	}

	public void setStages(final Collection<Stage> stages) {
		this.stages = stages;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Sponsorship> getSponsorships() {
		return this.sponsorships;
	}
	public void setSponsorships(final Collection<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Note> getNotes() {
		return this.notes;
	}
	public void setNotes(final Collection<Note> notes) {
		this.notes = notes;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Audit> getAudits() {
		return this.audits;
	}
	public void setAudits(final Collection<Audit> audits) {
		this.audits = audits;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Story> getStories() {
		return this.stories;
	}
	public void setStories(final Collection<Story> stories) {
		this.stories = stories;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Application> getApplications() {
		return this.applications;
	}
	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
	public Collection<SurvivalClass> getSurvivalClasses() {
		return this.survivalClasses;
	}
	public void setSurvivalClasses(final Collection<SurvivalClass> survivalClasses) {
		this.survivalClasses = survivalClasses;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
	public Collection<TagValue> getTagValues() {
		return this.tagValues;
	}
	public void setTagValues(final Collection<TagValue> tagValues) {
		this.tagValues = tagValues;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Ranger getRanger() {
		return this.ranger;
	}

	public void setRanger(final Ranger ranger) {
		this.ranger = ranger;
	}
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public LegalText getLegalText() {
		return this.legalText;
	}

	public void setLegalText(final LegalText legalText) {
		this.legalText = legalText;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

}
