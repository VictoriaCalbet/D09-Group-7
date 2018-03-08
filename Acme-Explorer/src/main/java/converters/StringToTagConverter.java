
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.TagRepository;
import domain.Tag;

public class StringToTagConverter implements Converter<String, Tag> {

	@Autowired
	TagRepository	tagRepository;


	@Override
	public Tag convert(final String source) {
		final Tag result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.tagRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
