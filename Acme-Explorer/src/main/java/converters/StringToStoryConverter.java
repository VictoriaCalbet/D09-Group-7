
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.StoryRepository;
import domain.Story;

public class StringToStoryConverter implements Converter<String, Story> {

	@Autowired
	StoryRepository	storyRepository;


	@Override
	public Story convert(final String source) {
		final Story result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.storyRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
