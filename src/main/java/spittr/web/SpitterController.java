package spittr.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spittr.config.annotation.WebController;
import spittr.data.SpitterRepository;
import spittr.entity.Spitter;

@WebController
@RequestMapping("/spitter")
public class SpitterController {
	private static final Logger logger = LoggerFactory.getLogger(SpitterController.class);

	private PasswordEncoder passwordEncoder;	
	private SpitterRepository spitterRepository;

	@Autowired
	public SpitterController(SpitterRepository spitterRepository,
			PasswordEncoder passwordEncoder) {
	    this.spitterRepository = spitterRepository;
	    this.passwordEncoder = passwordEncoder;
	}
  
    @RequestMapping(value="/register", method=GET)
    public String showRegistrationForm(Model model) {
    	model.addAttribute("registerForm", new RegisterForm());
    	return "registerForm";
    }
  
	@RequestMapping(value="/register", method=POST)
	public String processRegistration(
			@RequestPart("profilePicture") MultipartFile profilePicture,
			RedirectAttributes model, //Must precede validate
		    @Valid RegisterForm registerForm,		    
		    Errors errors) {
		
		String uploads = "/home/jvwong/Projects/spring/workspace/Spittr/tmp/uploads/";
	  
		if (errors.hasErrors()) {
			logger.info("Register form errors encountered");
			return "registerForm";
		}
			
		Spitter spitter = registerForm.toSpitter();
		spitter.setPassword(passwordEncoder.encode(spitter.getPassword()));
		spitter.setRole("ROLE_SPITTER");
		
		try{
			spitterRepository.save(spitter);
			
			if(!profilePicture.isEmpty()){
				try{				
					profilePicture.transferTo(
							new File(uploads + profilePicture.getOriginalFilename()));
					logger.info("Uploaded: {} to {}", profilePicture.getOriginalFilename(), uploads);
				} catch (IOException e){
					logger.error("Failed to upload image: {}", e);
				}
			}		 
						
			model.addAttribute("username", spitter.getUsername());
			model.addFlashAttribute("spitter", spitter);

			// When InternalResourceViewResolver sees the "redirect: " prefix
			// on the view specification, it will redirect 
			return "redirect:/spitter/{username}";
			
		} catch (DataIntegrityViolationException dee) {			
			String message = "Error: " + dee.getMostSpecificCause().getMessage();
			
			model.addFlashAttribute("message", message);
			return "redirect:/spitter/register";
		}	
	}
  
	@RequestMapping(value="/{username}", method=GET)
	public String showSpitterProfile(
			@PathVariable String username,
			Model model) {
				
		if(!model.containsAttribute("spitter")){
			   Spitter spitter = spitterRepository.findByUsername(username);
			    model.addAttribute(spitter);			    
		}
		
		return "profile";	 
	}
  
}
