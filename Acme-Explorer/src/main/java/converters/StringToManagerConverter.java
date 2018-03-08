
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ManagerRepository;
import domain.Manager;

public class StringToManagerConverter implements Converter<String, Manager> {

	@Autowired
	ManagerRepository	managerRepository;


	@Override
	public Manager convert(final String source) {
		final Manager result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.managerRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
