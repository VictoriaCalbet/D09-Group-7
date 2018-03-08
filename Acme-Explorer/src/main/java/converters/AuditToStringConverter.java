
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Audit;

public class AuditToStringConverter implements Converter<Audit, String> {

	@Override
	public String convert(final Audit source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
