
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Stage;

public class StageToStringConverter implements Converter<Stage, String> {

	@Override
	public String convert(final Stage source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
