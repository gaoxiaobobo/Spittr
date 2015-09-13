package spittr.rest;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import spittr.config.annotation.RestEndpoint;
import spittr.data.SpitterRepository;
import spittr.entity.Spitter;
import spittr.exceptions.GenericNotFoundException;


@RestEndpoint
@RequestMapping(value="/rest/spitter")
public class SpitterRestEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(SpitterRestEndpoint.class);	
	
	private SpitterRepository spitterRepository;	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public SpitterRestEndpoint(SpitterRepository spitterRepository,
			PasswordEncoder passwordEncoder){
		this.spitterRepository = spitterRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<Spitter> spitters(
			@RequestParam(value="count", defaultValue="20") int count){
		logger.info("GET rest/spitters: {}", "OK");
		return spitterRepository.findAll();		
	}
	
	@RequestMapping(value="/{spitterId}", method=RequestMethod.GET)
	public ResponseEntity<?> spitter(
	      @PathVariable("spitterId") long id) {
		  
		Spitter spitter = spitterRepository.findOne(id);		  
		if(spitter == null) throw new GenericNotFoundException(id, Spitter.class.getSimpleName()); 
		return new ResponseEntity<Spitter>(spitter, HttpStatus.OK);
	}
	
	@RequestMapping(
			value="",
			method=RequestMethod.POST, 
			consumes="application/json")
	public ResponseEntity<Spitter> saveSpitter(
			@RequestBody Spitter spitter,
			UriComponentsBuilder ucb){
		
		try{
			
			HttpHeaders headers = new HttpHeaders();
			
			// NullPointerException
			spitter.setPassword(passwordEncoder.encode(spitter.getPassword()));			
			// DataIntegrityViolationException
			Spitter spitterSaved = spitterRepository.save(spitter);
			
			URI locationUri = 
					ucb.path("/services/rest/spitter")
					   .path(String.valueOf(spitterSaved.getId()))
					   .build()
					   .toUri();				
			headers.setLocation(locationUri);
			return  new ResponseEntity<Spitter>(spitterSaved, headers, HttpStatus.CREATED);
			
		} catch (NullPointerException npe) {
			
			String message = "Error: " + npe.getMessage();
			logger.warn(message);
			return new ResponseEntity<Spitter>(HttpStatus.UNPROCESSABLE_ENTITY);
				
		} catch (DataIntegrityViolationException dee) {			
			
			String message = "Error: " + dee.getMostSpecificCause().getMessage();
			logger.warn(message);
			return new ResponseEntity<Spitter>(HttpStatus.CONFLICT);			
		}				
	}		
}
