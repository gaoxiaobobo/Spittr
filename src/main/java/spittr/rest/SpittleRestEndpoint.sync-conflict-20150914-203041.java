package spittr.rest;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import spittr.config.annotation.RestEndpoint;
import spittr.data.SpitterRepository;
import spittr.data.SpittleRepository;
import spittr.entity.Spitter;
import spittr.entity.Spittle;
import spittr.exceptions.GenericNotFoundException;
import spittr.exceptions.MissingEntityException;


@RestEndpoint
@RequestMapping(value="/spittles")
public class SpittleRestEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(SpittleRestEndpoint.class);	
	
	private SpittleRepository spittleRepository;
	private SpitterRepository spitterRepository;
	
	@Autowired
	public SpittleRestEndpoint(SpittleRepository spittleRepository,
			SpitterRepository spitterRepository){
		this.spittleRepository = spittleRepository;
		this.spitterRepository = spitterRepository;
	}
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<Spittle> spittles(
			@RequestParam(value="count", defaultValue="20") int count){
		logger.info("GET rest/spittles: {}", "OK");
		return spittleRepository.findAll();		
	}
	
	@RequestMapping(value="/{spittleId}", method=RequestMethod.GET)
	public Spittle spittle(
	      @PathVariable("spittleId") long id) {
		  
		Spittle spittle = spittleRepository.findOne(id);		  
		if(spittle == null) throw new GenericNotFoundException(id, Spittle.class.getSimpleName()); 
		return spittle;
	}
	
	@RequestMapping(
			value="",
			method=RequestMethod.POST, 
			consumes="application/json")
	public Spittle saveSpittle(
			@RequestBody Spittle spittle,			
			UriComponentsBuilder ucb){
		
		Spitter spitter = spitterRepository.findOne(spittle.getSpitter().getId());
		if(spitter == null) throw new MissingEntityException(Spitter.class.getName()); 
		
		spittle.setSpitter(spitter);		
		Spittle spittleSaved = spittleRepository.save(spittle);
		
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = 
				ucb.path("/services/rest/spittles")
				   .path(String.valueOf(spittleSaved.getId()))
				   .build()
				   .toUri();				
		headers.setLocation(locationUri);
		return spittle;		
	}		
}
