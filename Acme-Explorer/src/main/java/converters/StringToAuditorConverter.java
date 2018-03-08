
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.AuditorRepository;
import domain.Auditor;

public class StringToAuditorConverter implements Converter<String, Auditor> {

	@Autowired
	AuditorRepository	auditorRepository;


	@Override
	public Auditor convert(final String source) {
		final Auditor result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.auditorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
