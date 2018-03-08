
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.StageRepository;
import domain.Stage;

public class StringToStageConverter implements Converter<String, Stage> {

	@Autowired
	StageRepository	stageRepository;


	@Override
	public Stage convert(final String source) {
		final Stage result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.stageRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
