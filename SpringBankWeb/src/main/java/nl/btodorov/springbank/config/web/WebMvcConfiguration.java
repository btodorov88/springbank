package nl.btodorov.springbank.config.web;

import nl.btodorov.springbank.mvc.SpringBankWebUIController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.tiles2.dialect.TilesDialect;
import org.thymeleaf.extras.tiles2.spring4.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.extras.tiles2.spring4.web.view.ThymeleafTilesView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Responsible to configure the web (MVC) part of the application
 *
 */
@EnableWebMvc
@ComponentScan(basePackageClasses = {WebMvcConfiguration.class, SpringBankWebUIController.class})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("classpath:/resources/")
				.setCachePeriod(31556926);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	/**
	 * Sets up the template resolver
	 * @return
	 */
	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		ClassLoaderTemplateResolver result = new ClassLoaderTemplateResolver();
		result.setPrefix("views/");
		result.setSuffix(".html");
		result.setTemplateMode("HTML5");
		return result;
	}

	/**
	 * Sets the configuration of the Tiles engine
	 * @return
	 */
	@Bean
	public ThymeleafTilesConfigurer tilesConfigurer() {
		ThymeleafTilesConfigurer tilesConfigurer = new ThymeleafTilesConfigurer();
		tilesConfigurer
				.setDefinitions(new String[] { "classpath:tiles/tiles-def.xml" });
		return tilesConfigurer;
	}

	/**
	 * Sets up the template engine
	 * 
	 * @param templateResolver
	 * @return
	 */
	@Bean
	public SpringTemplateEngine templateEngine(
			ClassLoaderTemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new TilesDialect());
		return templateEngine;
	}

	/**
	 * Sets up the template view resolver
	 * @param templateEngine
	 * @return
	 */
	@Bean
	public ThymeleafViewResolver viewResolver(
			SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setViewClass(ThymeleafTilesView.class);
		viewResolver.setCache(false);
		return viewResolver;
	}
}