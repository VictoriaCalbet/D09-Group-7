
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Explorer;

public class ExplorerToStringConverter implements Converter<Explorer, String> {

	@Override
	public String convert(final Explorer source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
