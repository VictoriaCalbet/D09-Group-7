
package converters;

import org.springframework.core.convert.converter.Converter;

import security.Authority;

public class StringToAuthorityConverter implements Converter<String, Authority> {

	@Override
	public Authority convert(final String source) {
		Authority result;

		try {
			result = new Authority();
			result.setAuthority(source);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
