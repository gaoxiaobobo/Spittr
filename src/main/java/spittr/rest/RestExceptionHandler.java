package spittr.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import spittr.config.annotation.RestEndpointAdvice;
import spittr.exceptions.Error;
import spittr.exceptions.GenericNotFoundException;
import spittr.exceptions.MissingCredentialsException;
import spittr.exceptions.MissingEntityException;


@RestEndpointAdvice
public class RestExceptionHandler {
  
	@ExceptionHandler(GenericNotFoundException.class)
	@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public Error objectNotFound(GenericNotFoundException e) {		
		return new Error(404, e.getClassName() + " [" + e.getId() + "] not found");	    
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error objectNotFound(BadCredentialsException e) {		
		return new Error(401, e.getMessage());	    
	}
	
	@ExceptionHandler(MissingEntityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error objectNotFound(MissingEntityException e) {		
		return new Error(400, "Bad/missing " + e.getClassName());	    
	}
	
	@ExceptionHandler(MissingCredentialsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error objectNotFound(MissingCredentialsException e) {		
		return new Error(400, e.getClassName() + " missing credential [" + e.getMessage() + "]");	    
	}

}