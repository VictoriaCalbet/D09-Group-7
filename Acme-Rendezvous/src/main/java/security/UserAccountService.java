
package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public UserAccount create(final String authority) {
		UserAccount result;
		Authority au;
		final Collection<Authority> authorities = new ArrayList<Authority>();

		au = new Authority();

		au.setAuthority(authority);

		authorities.add(au);

		result = new UserAccount();

		result.setAuthorities(authorities);

		return result;
	}

	// Other business methods -------------------------------------------------
	public UserAccount createComplete(final String username, final String Password, final String authority) {
		UserAccount result;

		result = this.create(authority);

		result.setUsername(username);
		result.setPassword(Password);

		result = this.modifyPassword(result);

		return result;
	}

	/**
	 * This method hashCode the password
	 */
	public UserAccount modifyPassword(final UserAccount input) {
		Md5PasswordEncoder encoder;
		String newPassword;

		encoder = new Md5PasswordEncoder();

		newPassword = encoder.encodePassword(input.getPassword(), null);

		input.setPassword(newPassword);

		return input;
	}

	public UserAccount findByUsername(final String username) {
		UserAccount result;

		result = this.userAccountRepository.findByUsername(username);

		return result;
	}

}
