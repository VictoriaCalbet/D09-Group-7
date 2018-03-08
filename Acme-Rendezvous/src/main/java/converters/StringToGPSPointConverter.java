
package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.GPSPoint;

@Component
@Transactional
public class StringToGPSPointConverter implements Converter<String, GPSPoint> {

	@Override
	public GPSPoint convert(final String text) {
		GPSPoint result;
		String parts[];

		if (text == null)
			result = null;
		else
			try {
				parts = text.split("\\|");
				result = new GPSPoint();

				result.setLatitude(Double.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				result.setLongitude(Double.valueOf(URLDecoder.decode(parts[1], "UTF-8")));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}
}
