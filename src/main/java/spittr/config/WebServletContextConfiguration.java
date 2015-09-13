package spittr.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import spittr.config.annotation.WebController;
import spittr.config.annotation.WebControllerAdvice;

/* Take over servlet-context.xml configuration */
@Configuration
@EnableWebMvc 
@ComponentScan(
        basePackages = "spittr.web",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter({WebController.class, WebControllerAdvice.class})
)
public class WebServletContextConfiguration extends WebMvcConfigurerAdapter {

	/*
	 * Declare view resolver
	 * Resolves template views from logical view names
	 */
	@Bean
	public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		return viewResolver;
	}	
	
	/*
	 * Declare template engine 
	 * Processes the templates and renders results
	 */
	@Bean
	public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);	
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}	
		
	/* 
	 * Declare template resolver
	 * Locates templates 
	 */
	@Bean
	public TemplateResolver templateResolver() {
		TemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");		
		return templateResolver;
	}	
	
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {	    
	    super.addResourceHandlers(registry);
	}
	
//	@Bean
//	public MessageSource messageSource(){
//		ReloadableResourceBundleMessageSource messageSource =
//				new ReloadableResourceBundleMessageSource();
//		messageSource.setBasename("/WEB-INF/messages");
//		messageSource.setCacheSeconds(10);
//		return messageSource;	
//	}
	
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
		return new StandardServletMultipartResolver();
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");		
	}	
}
