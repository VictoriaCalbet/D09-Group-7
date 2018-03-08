
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.TagValue;

public class TagValueToStringConverter implements Converter<TagValue, String> {

	@Override
	public String convert(final TagValue source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
