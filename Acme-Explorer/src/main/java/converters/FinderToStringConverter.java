
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Finder;

public class FinderToStringConverter implements Converter<Finder, String> {

	@Override
	public String convert(final Finder source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
