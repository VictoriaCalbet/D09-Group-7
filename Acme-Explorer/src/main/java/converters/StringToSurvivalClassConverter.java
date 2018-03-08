
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.SurvivalClassRepository;
import domain.SurvivalClass;

public class StringToSurvivalClassConverter implements Converter<String, SurvivalClass> {

	@Autowired
	SurvivalClassRepository	survivalClassRepository;


	@Override
	public SurvivalClass convert(final String source) {
		final SurvivalClass result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.survivalClassRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
