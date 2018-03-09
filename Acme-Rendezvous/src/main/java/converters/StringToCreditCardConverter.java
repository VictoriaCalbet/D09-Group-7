
package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;

import domain.CreditCard;

public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Override
	public CreditCard convert(final String source) {
		final CreditCard result;
		final String parts[];

		if (source == null)
			result = null;
		else
			try {
				parts = source.split("\\|");
				result = new CreditCard();
				result.setHolderName(URLDecoder.decode(parts[0], "UTF-8"));
				result.setBrandName(URLDecoder.decode(parts[1], "UTF-8"));
				result.setNumber(URLDecoder.decode(parts[2], "UTF-8"));
				result.setExpirationMonth(Integer.valueOf(URLDecoder.decode(parts[3], "UTF-8")));
				result.setExpirationYear(Integer.valueOf(URLDecoder.decode(parts[4], "UTF-8")));
				result.setCvv(Integer.valueOf(URLDecoder.decode(parts[5], "UTF-8")));
			} catch (final Throwable oops) {
				throw new RuntimeException();
			}

		return result;
	}
}
