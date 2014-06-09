package nl.btodorov.springbank.config.web;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Makes sure the springSecurityFilterChain is registered before any other filter
 *
 */
public class SecurityInitializer extends
		AbstractSecurityWebApplicationInitializer {

}
