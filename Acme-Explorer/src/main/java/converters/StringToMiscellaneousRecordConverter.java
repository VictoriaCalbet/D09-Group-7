
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.MiscellaneousRecordRepository;
import domain.MiscellaneousRecord;

public class StringToMiscellaneousRecordConverter implements Converter<String, MiscellaneousRecord> {

	@Autowired
	MiscellaneousRecordRepository	miscellaneousRecordRepository;


	@Override
	public MiscellaneousRecord convert(final String source) {
		final MiscellaneousRecord result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.miscellaneousRecordRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
