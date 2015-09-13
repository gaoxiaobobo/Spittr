package spittr.rest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import spittr.config.annotation.RestEndpoint;
import spittr.data.SpitterRepository;
import spittr.entity.Spitter;
import spittr.exceptions.MissingCredentialsException;
import spittr.exceptions.MissingEntityException;
import spittr.security.TokenAuthenticationService;

@RestEndpoint
@RequestMapping(value="/rest/login")
public class AuthRestEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(AuthRestEndpoint.class);	
	
	private TokenAuthenticationService tokenAuthenticationService;
	private AuthenticationManager authenticationManager;
	private SpitterRepository spitterRepository;
	
	@Autowired
	public AuthRestEndpoint(
			TokenAuthenticationService tokenAuthenticationService,
			SpitterRepository spitterRepository,
			AuthenticationManager authenticationManager){
		this.spitterRepository = spitterRepository;
		this.tokenAuthenticationService = tokenAuthenticationService;		
		this.authenticationManager = authenticationManager;
	}
	
	@RequestMapping(
			value="",
			method=RequestMethod.POST, 
			consumes="application/json")
	public Spitter login(@RequestBody Spitter spitter,
			HttpServletResponse  httpResponse){
						
		// Throws NullPointerException
		if(spitter.getUsername() == null){
			throw new MissingCredentialsException("username",  Spitter.class.getSimpleName());
		}				
	
		if(spitter.getPassword() == null){
			throw new MissingCredentialsException("password",  Spitter.class.getSimpleName());
		} 
			
		Authentication authRequest = new UsernamePasswordAuthenticationToken(spitter.getUsername(),
				spitter.getPassword());	
		
		// Note: Implicitly this process throws a BadCredentialsException if
		// authentication fails, which is caught within our RestExceptionHandler.
		authenticationManager.authenticate(authRequest);			
		
		// If it succeeds only then will the addAuthentication proceed.
		Spitter savedSpitter = spitterRepository.findByUsername(spitter.getUsername());
		tokenAuthenticationService.addAuthentication(httpResponse, savedSpitter);
		
		if(savedSpitter == null) throw new MissingEntityException(Spitter.class.getName()); 
		
		return savedSpitter;	
	}		
}



