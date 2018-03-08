
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.SponsorRepository;
import domain.Sponsor;

public class StringToSponsorConverter implements Converter<String, Sponsor> {

	@Autowired
	SponsorRepository	sponsorRepository;


	@Override
	public Sponsor convert(final String source) {
		final Sponsor result;
		final int id;

		try {
			if (StringUtils.isEmpty(source))
				result = null;
			else {
				id = Integer.valueOf(source);
				result = this.sponsorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
		return result;
	}
}
