
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Location extends DomainEntity {

	private String		name;
	private GPSPoint	gpsPoint;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@Valid
	public GPSPoint getGpsPoint() {
		return this.gpsPoint;
	}
	public void setGpsPoint(final GPSPoint gpsPoint) {
		this.gpsPoint = gpsPoint;
	}

}
