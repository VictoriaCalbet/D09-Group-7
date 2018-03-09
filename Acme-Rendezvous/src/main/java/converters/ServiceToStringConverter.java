
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Service;

@Component
@Transactional
public class ServiceToStringConverter implements Converter<Service, String> {

	@Override
	public String convert(final Service manager) {
		String result;

		if (manager == null)
			result = null;
		else
			result = String.valueOf(manager.getId());
		return result;
	}

}
