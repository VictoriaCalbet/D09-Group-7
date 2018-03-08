
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Message;

public class MessageToStringConverter implements Converter<Message, String> {

	@Override
	public String convert(final Message source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());
		return result;
	}
}
