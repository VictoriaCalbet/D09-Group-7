
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ContactEmergencyRepository;
import domain.ContactEmergency;

public class StringToContactEmergencyConverter implements Converter<String, ContactEmergency> {

	@Autowired
	ContactEmergencyRepository	contactEmergencyRepository;


	@Override
	public ContactEmergency convert(final String source) {
		final ContactEmergency result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.contactEmergencyRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
