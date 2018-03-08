
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Category;

public class CategoryToStringConverter implements Converter<Category, String> {

	@Override
	public String convert(final Category source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
