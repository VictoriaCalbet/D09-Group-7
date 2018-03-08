
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.LocationRepository;
import domain.Location;

public class StringToLocationConverter implements Converter<String, Location> {

	@Autowired
	LocationRepository	locationRepository;


	@Override
	public Location convert(final String source) {
		final Location result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.locationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
