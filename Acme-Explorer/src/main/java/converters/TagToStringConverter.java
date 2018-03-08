
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Tag;

public class TagToStringConverter implements Converter<Tag, String> {

	@Override
	public String convert(final Tag source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
