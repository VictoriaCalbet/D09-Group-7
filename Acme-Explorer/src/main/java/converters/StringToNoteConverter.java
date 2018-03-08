
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.NoteRepository;
import domain.Note;

public class StringToNoteConverter implements Converter<String, Note> {

	@Autowired
	NoteRepository	noteRepository;


	@Override
	public Note convert(final String source) {
		final Note result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.noteRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
