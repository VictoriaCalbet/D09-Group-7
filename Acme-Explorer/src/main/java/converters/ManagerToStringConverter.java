
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Manager;

public class ManagerToStringConverter implements Converter<Manager, String> {

	@Override
	public String convert(final Manager source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
