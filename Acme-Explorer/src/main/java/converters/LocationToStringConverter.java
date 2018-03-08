
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Location;

public class LocationToStringConverter implements Converter<Location, String> {

	@Override
	public String convert(final Location source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
