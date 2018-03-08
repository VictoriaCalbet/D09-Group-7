
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Folder;

public class FolderToStringConverter implements Converter<Folder, String> {

	@Override
	public String convert(final Folder source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
