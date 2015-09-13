package spittr.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Bootstrap implements WebApplicationInitializer {
	
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
    	/*
    	 * Root application context configuration
    	 */
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        /*
    	 * Web UI application context configuration
    	 */
        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebServletContextConfiguration.class);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "springWebDispatcher", new DispatcherServlet(webContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.setMultipartConfig(
				new MultipartConfigElement(
						"/home/jeffrey/Sync/Projects/sts-workspace/Spittr/src/main/webapp/tmp/spittr/uploads",
						2097152, // max size (bytes) file
						4194304, // max size (bytes) total request
						0));	
        dispatcher.addMapping("/");
        
        /*
    	 * RESTful web service application context configuration
    	 */
        AnnotationConfigWebApplicationContext restContext =
                new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(restContext);
        servlet.setDispatchOptionsRequest(true);
        dispatcher = servletContext.addServlet(
                "springRestDispatcher", servlet
        );
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/services/*");
        
//        FilterRegistration.Dynamic registration = servletContext.addFilter("filterA", new FilterA());
//        registration.addMappingForUrlPatterns(null, false, "/*");
    }
}
