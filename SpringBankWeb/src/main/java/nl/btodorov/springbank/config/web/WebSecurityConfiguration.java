package nl.btodorov.springbank.config.web;

import nl.btodorov.springbank.mvc.sequrity.UserRepositoryUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Sets up spring security
 *
 */
@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackageClasses = UserRepositoryUserDetailsService.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * Makes sure the authentication is performed using custom defined
	 * {@link UserDetailsService}
	 * 
	 * @param userDetailsService
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(UserDetailsService userDetailsService,
			AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

}
