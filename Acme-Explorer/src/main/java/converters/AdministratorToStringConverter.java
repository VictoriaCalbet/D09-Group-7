
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Administrator;

public class AdministratorToStringConverter implements Converter<Administrator, String> {

	@Override
	public String convert(final Administrator source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
