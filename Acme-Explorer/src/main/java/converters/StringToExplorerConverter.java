
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ExplorerRepository;
import domain.Explorer;

public class StringToExplorerConverter implements Converter<String, Explorer> {

	@Autowired
	ExplorerRepository	explorerRepository;


	@Override
	public Explorer convert(final String source) {
		final Explorer result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.explorerRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
