
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.SystemConfiguration;

public class SystemConfigurationToStringConverter implements Converter<SystemConfiguration, String> {

	@Override
	public String convert(final SystemConfiguration source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
