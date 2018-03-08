
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Curriculum;

public class CurriculumToStringConverter implements Converter<Curriculum, String> {

	@Override
	public String convert(final Curriculum source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
