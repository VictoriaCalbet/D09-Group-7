
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Auditor;

public class AuditorToStringConverter implements Converter<Auditor, String> {

	@Override
	public String convert(final Auditor source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
