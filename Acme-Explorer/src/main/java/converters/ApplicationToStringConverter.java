
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Application;

public class ApplicationToStringConverter implements Converter<Application, String> {

	@Override
	public String convert(final Application source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
