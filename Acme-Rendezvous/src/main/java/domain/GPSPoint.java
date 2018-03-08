
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Range;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSPoint {

	// Attributes -------------------------------------------------------------

	private Double	latitude;
	private Double	longitude;


	@Range(min = (long) -180.0, max = (long) 180.0)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}

	@Range(min = (long) -90.0, max = (long) 90.0)
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

	// Relationships ----------------------------------------------------------
}
