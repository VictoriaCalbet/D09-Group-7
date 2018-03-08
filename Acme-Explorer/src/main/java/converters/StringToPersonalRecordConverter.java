
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.PersonalRecordRepository;
import domain.PersonalRecord;

public class StringToPersonalRecordConverter implements Converter<String, PersonalRecord> {

	@Autowired
	PersonalRecordRepository	personalRecordRepository;


	@Override
	public PersonalRecord convert(final String source) {
		final PersonalRecord result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.personalRecordRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
