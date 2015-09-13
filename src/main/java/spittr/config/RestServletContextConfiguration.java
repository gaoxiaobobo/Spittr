package spittr.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import spittr.config.annotation.RestEndpoint;
import spittr.config.annotation.RestEndpointAdvice;

@Configuration
@EnableWebMvc 
@ComponentScan(
        basePackages = "spittr.rest",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter({
        	RestEndpoint.class,
        	RestEndpointAdvice.class})
)
public class RestServletContextConfiguration extends WebMvcConfigurerAdapter {
}
