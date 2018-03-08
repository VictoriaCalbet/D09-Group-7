
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.LegalText;

public class LegalTextToStringConverter implements Converter<LegalText, String> {

	@Override
	public String convert(final LegalText source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
