
package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;

import domain.GPSPoint;

public class StringToGPSPointConverter implements Converter<String, GPSPoint> {

	@Override
	public GPSPoint convert(final String source) {
		final GPSPoint result;
		final String parts[];

		if (source == null)
			result = null;
		else
			try {
				parts = source.split("\\|");
				result = new GPSPoint();
				result.setLatitude(Double.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				result.setLongitude(Double.valueOf(URLDecoder.decode(parts[1], "UTF-8")));
			} catch (final Throwable oops) {
				throw new RuntimeException();
			}

		return result;
	}
}
