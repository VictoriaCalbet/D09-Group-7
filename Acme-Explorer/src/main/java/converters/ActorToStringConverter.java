
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Actor;

public class ActorToStringConverter implements Converter<Actor, String> {

	@Override
	public String convert(final Actor source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
