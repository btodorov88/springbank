package nl.btodorov.springbank.config;

import nl.btodorov.springbank.config.data.DataConfiguration;
import nl.btodorov.springbank.config.web.SecurityInitializer;
import nl.btodorov.springbank.service.AccountService;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Responsible for the root configuration of the application. Defines the data
 * and service configs that will be used.
 */
@Configuration
@ComponentScan(basePackageClasses = { DataConfiguration.class,
		AccountService.class, SecurityInitializer.class })
public class RootConfiguration {

}