
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Ranger;

public class RangerToStringConverter implements Converter<Ranger, String> {

	@Override
	public String convert(final Ranger source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
