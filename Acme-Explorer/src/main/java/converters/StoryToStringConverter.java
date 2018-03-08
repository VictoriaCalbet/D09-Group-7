
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Story;

public class StoryToStringConverter implements Converter<Story, String> {

	@Override
	public String convert(final Story source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
