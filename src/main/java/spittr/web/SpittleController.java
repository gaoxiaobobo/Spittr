package spittr.web;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spittr.config.annotation.WebController;
import spittr.data.SpitterRepository;
import spittr.data.SpittleRepository;
import spittr.entity.Spitter;
import spittr.entity.Spittle;

@WebController
@RequestMapping("/spittles")
public class SpittleController {
	private static final Logger logger = LoggerFactory.getLogger(SpittleController.class);
	  
	private SpittleRepository spittleRepository;
	private SpitterRepository spitterRepository;
	
	@Autowired
	public SpittleController(SpittleRepository spittleRepository,
			SpitterRepository spitterRepository) {
	    this.spittleRepository = spittleRepository;
	    this.spitterRepository = spitterRepository;
	}

   /* When a handler method returns an object or a collection 
    * the returned value is put in to the model, and the model 
    * key is inferred from its type
    */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String spittles(      
	      @RequestParam(value="count", defaultValue="20") int count,
	      Model model) {	  
		  List<Spittle> list = spittleRepository.findAll();	  
		  model.addAttribute("spittleForm", new SpittleForm());
		  model.addAttribute("spittleList", list);
	    return "spittles";
	}
	
	@RequestMapping(value="/{spittleId}", method=RequestMethod.GET)
	public String spittle(
	      @PathVariable("spittleId") long spittleId, 
	      Model model) {
		  
		  Spittle spittle = spittleRepository.findOne(spittleId);
		  if(spittle == null) throw new SpittleNotFoundException();
		  
		  model.addAttribute("spittle", spittle);
		  return "spittle";
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	public String saveSpittle(
			Principal principal,
			RedirectAttributes model,
			@Valid SpittleForm spittleForm,
			Errors errors){
		
		if (errors.hasErrors()) {
			logger.info("Spittle errors encountered");
			model.addFlashAttribute("errors", errors);
			return "spittles";   
		}
		
		/* stub out the spitter for now */
		Spitter spitter = spitterRepository.findByUsername(principal.getName());		
		Spittle spittle = new Spittle();		
		spittle.setMessage(spittleForm.getMessage());
//		DateTime now = new DateTime();
//		spittle.setCreatedDate(now);
//		spittle.setLastModifiedDate(now);
		spittle.setSpitter(spitter);	
		
		spittleRepository.save(spittle);
		
		return "redirect:/spittles";    
	}

}
