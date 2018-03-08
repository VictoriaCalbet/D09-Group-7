
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isDeleted"), @Index(columnList = "isDraft"), @Index(columnList = "isAdultOnly"), @Index(columnList = "meetingMoment")
})
public class Rendezvous extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String		name;
	private String		description;
	private Date		meetingMoment;
	private String		picture;
	private GPSPoint	gpsPoint;
	private boolean		isDraft;
	private boolean		isDeleted;
	private boolean		isAdultOnly;


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

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMeetingMoment() {
		return this.meetingMoment;
	}

	public void setMeetingMoment(final Date meetingMoment) {
		this.meetingMoment = meetingMoment;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@Valid
	public GPSPoint getGpsPoint() {
		return this.gpsPoint;
	}

	public void setGpsPoint(final GPSPoint gpsPoint) {
		this.gpsPoint = gpsPoint;
	}

	public boolean getIsDraft() {
		return this.isDraft;
	}

	public void setIsDraft(final boolean isDraft) {
		this.isDraft = isDraft;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(final boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsAdultOnly() {
		return this.isAdultOnly;
	}

	public void setIsAdultOnly(final boolean isAdultOnly) {
		this.isAdultOnly = isAdultOnly;
	}


	// Relationships ----------------------------------------------------------

	private User						creator;
	private Collection<RSVP>			rsvps;
	private Collection<Question>		questions;
	private Collection<Comment>			comments;
	private Collection<Announcement>	announcements;
	private Collection<Rendezvous>		isLinkedTo;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "rendezvous", cascade = CascadeType.REMOVE)
	public Collection<RSVP> getRsvps() {
		return this.rsvps;
	}

	public void setRsvps(final Collection<RSVP> rsvps) {
		this.rsvps = rsvps;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "rendezvous", cascade = CascadeType.REMOVE)
	public Collection<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(final Collection<Question> questions) {
		this.questions = questions;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "rendezvous", cascade = CascadeType.REMOVE)
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "rendezvous", cascade = CascadeType.REMOVE)
	public Collection<Announcement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnnouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Rendezvous> getIsLinkedTo() {
		return this.isLinkedTo;
	}

	public void setIsLinkedTo(final Collection<Rendezvous> isLinkedTo) {
		this.isLinkedTo = isLinkedTo;
	}
}
