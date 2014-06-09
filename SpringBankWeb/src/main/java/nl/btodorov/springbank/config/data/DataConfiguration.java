package nl.btodorov.springbank.config.data;

import javax.sql.DataSource;

import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.repository.AccountRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Initializes spring Configuration for Spring Data.
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = AccountRepository.class)
public class DataConfiguration {

	/**
	 * Sets up the usage of an embedded H2 database
	 * 
	 * @return
	 */
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.H2);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(Person.class.getPackage().getName());
		factory.setDataSource(dataSource());

		return factory;
	}

	/**
	 * Initializes the database setting up the db schema and initial data.
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	@DependsOn("entityManagerFactory")
	public ResourceDatabasePopulator initDatabase(DataSource dataSource)
			throws Exception {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("sql/schema.sql"));
		populator.addScript(new ClassPathResource("sql/test-data.sql"));
		populator.populate(dataSource.getConnection());
		return populator;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}
}
