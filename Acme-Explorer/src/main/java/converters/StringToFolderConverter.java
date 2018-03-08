
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.FolderRepository;
import domain.Folder;

public class StringToFolderConverter implements Converter<String, Folder> {

	@Autowired
	FolderRepository	folderRepository;


	@Override
	public Folder convert(final String source) {
		final Folder result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.folderRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
