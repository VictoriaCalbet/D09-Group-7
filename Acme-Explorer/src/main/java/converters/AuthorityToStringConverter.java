
package converters;

import org.springframework.core.convert.converter.Converter;

import security.Authority;

public class AuthorityToStringConverter implements Converter<Authority, String> {

	@Override
	public String convert(final Authority source) {
		final String result;

		if (source == null)
			result = null;
		else
			result = source.getAuthority();
		return result;
	}
}
