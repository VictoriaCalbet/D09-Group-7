
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.TagValueRepository;
import domain.TagValue;

public class StringToTagValueConverter implements Converter<String, TagValue> {

	@Autowired
	TagValueRepository	tagValueRepository;


	@Override
	public TagValue convert(final String source) {
		final TagValue result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.tagValueRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
