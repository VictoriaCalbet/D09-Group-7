
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.ContactEmergency;

public class ContactEmergencyToStringConverter implements Converter<ContactEmergency, String> {

	@Override
	public String convert(final ContactEmergency source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
