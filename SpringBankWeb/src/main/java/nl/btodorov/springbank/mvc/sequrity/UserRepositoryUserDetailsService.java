package nl.btodorov.springbank.mvc.sequrity;

import java.util.Collection;

import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of a {@link UserDetailsService} which provides the bridge
 * between the users in our database and spring security. This implementation is
 * needed for the authentication of the users
 *
 */
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private PersonRepository personRepository;

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Person person = personRepository.findByUsername(username);
		if (person == null) {
			throw new UsernameNotFoundException("Could not find user!");
		}
		return new SpringBankUserDetails(person);
	}

	/**
	 * Provides adaptor between {@link Person} and {@link UserDetails}
	 *
	 */
	@SuppressWarnings("serial")
	private final static class SpringBankUserDetails extends Person implements
			UserDetails {

		public SpringBankUserDetails(Person person) {
			super(person);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}

		@Override
		public String getPassword() {
			return super.getPassword();
		}

		@Override
		public String getUsername() {
			return super.getUsername();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}

}
