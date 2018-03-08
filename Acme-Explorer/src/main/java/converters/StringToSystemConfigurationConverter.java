
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

public class StringToSystemConfigurationConverter implements Converter<String, SystemConfiguration> {

	@Autowired
	SystemConfigurationRepository	systemConfigurationRepository;


	@Override
	public SystemConfiguration convert(final String source) {
		final SystemConfiguration result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.systemConfigurationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
