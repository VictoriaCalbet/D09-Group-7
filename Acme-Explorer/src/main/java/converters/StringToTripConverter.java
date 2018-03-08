
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.TripRepository;
import domain.Trip;

public class StringToTripConverter implements Converter<String, Trip> {

	@Autowired
	TripRepository	tripRepository;


	@Override
	public Trip convert(final String source) {
		final Trip result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.tripRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
