
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.RangerRepository;
import domain.Ranger;

public class StringToRangerConverter implements Converter<String, Ranger> {

	@Autowired
	RangerRepository	rangerRepository;


	@Override
	public Ranger convert(final String source) {
		final Ranger result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.rangerRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
