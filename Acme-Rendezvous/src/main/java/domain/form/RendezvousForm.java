
package domain.form;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import domain.GPSPoint;

public class RendezvousForm {

	// Attributes -------------------------------------------------------------

	private int			id;
	private String		name;
	private String		description;
	private Date		meetingMoment;
	private String		picture;
	private GPSPoint	gpsPoint;
	private boolean		isDraft;
	private boolean		isAdultOnly;


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

	public boolean getIsAdultOnly() {
		return this.isAdultOnly;
	}

	public void setIsAdultOnly(final boolean isAdultOnly) {
		this.isAdultOnly = isAdultOnly;
	}

}
