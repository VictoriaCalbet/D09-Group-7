
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;

import domain.GPSPoint;

public class GPSPointToStringConverter implements Converter<GPSPoint, String> {

	@Override
	public String convert(final GPSPoint source) {
		final String result;
		final StringBuilder builder;

		if (source == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(Double.toString(source.getLatitude()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(source.getLongitude()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
