
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.AuditRepository;
import domain.Audit;

public class StringToAuditConverter implements Converter<String, Audit> {

	@Autowired
	AuditRepository	auditRepository;


	@Override
	public Audit convert(final String source) {
		final Audit result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.auditRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
