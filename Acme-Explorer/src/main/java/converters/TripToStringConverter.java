
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Trip;

public class TripToStringConverter implements Converter<Trip, String> {

	@Override
	public String convert(final Trip source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
