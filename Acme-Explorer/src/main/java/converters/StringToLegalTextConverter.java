
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.LegalTextRepository;
import domain.LegalText;

public class StringToLegalTextConverter implements Converter<String, LegalText> {

	@Autowired
	LegalTextRepository	legalTextRepository;


	@Override
	public LegalText convert(final String source) {
		final LegalText result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.legalTextRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
