
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;

import domain.CreditCard;

public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard source) {
		final String result;
		final StringBuilder builder;

		if (source == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(source.getHolderName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(source.getBrandName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(source.getNumber(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(source.getExpirationMonth()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(source.getExpirationYear()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(source.getCvv()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
