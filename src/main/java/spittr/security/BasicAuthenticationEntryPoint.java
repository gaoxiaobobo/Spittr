package spittr.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.Assert;

/**
* Used by the <code>ExceptionTraslationFilter</code> to commence authentication via the
* {@link BasicAuthenticationFilter}.
* <p>
* Once a user agent is authenticated using BASIC authentication, logout requires that the
* browser be closed or an unauthorized (401) header be sent. The simplest way of
* achieving the latter is to call the
* {@link #commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
* method below. This will indicate to the browser its credentials are no longer
* authorized, causing it to prompt the user to login again.
*
* @author jvwong
*/
public class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint,
		InitializingBean {
	// ~ Instance fields
	// ================================================================================================
	
	private String realmName = "Spittr";

	// ~ Methods
	// ========================================================================================================

	public void afterPropertiesSet() throws Exception {
		Assert.hasText(realmName, "realmName must be specified");
	}

	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

}

	

