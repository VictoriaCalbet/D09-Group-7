
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSPoint {

	// Attributes
	private double	latitude;
	private double	longitude;


	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

}
