
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Note;

public class NoteToStringConverter implements Converter<Note, String> {

	@Override
	public String convert(final Note source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
