
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.GPSPoint;

@Component
@Transactional
public class GPSPointToStringConverter implements Converter<GPSPoint, String> {

	@Override
	public String convert(final GPSPoint gpsPoint) {
		String result;
		StringBuilder builder;

		if (gpsPoint == null)
			result = null;
		else
			try {
				builder = new StringBuilder();

				builder.append(URLEncoder.encode(Double.toString(gpsPoint.getLatitude()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(gpsPoint.getLongitude()), "UTF-8"));

				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
