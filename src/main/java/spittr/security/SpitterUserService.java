
package spittr.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;

/**
 * UserDetailService that is asked by DaoAuthenticationProvider to validate
 * a UsernamePasswordAuthenticationToken created upstream of login by the 
 * UsernamePasswordAuthenticationFilter upon an AccessDeniedException
 * @author jvwong
 *
 */
@Component
public class SpitterUserService implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(SpitterUserService.class);
	private final SpitterRepository spitterRepository;
	
	@Autowired
	public SpitterUserService(SpitterRepository spitterRepository) {
		this.spitterRepository = spitterRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Spitter spitter = spitterRepository.findByUsername(username);
		
		//logger.info("spitter found: {}", spitter.getUsername());
				
		if(spitter != null){
			List<GrantedAuthority> authorities = 
					new ArrayList<GrantedAuthority>();
			
			// Stub, should access the list of roles
			authorities.add(new SimpleGrantedAuthority(spitter.getRole()));
		
			// Spring will check against this user
			// Better to implement UserDetails and merge with Spitter
			return new User(
					spitter.getUsername(),
					spitter.getPassword(),
					authorities);
		}		
		
		throw new UsernameNotFoundException("User '" + username + "' not found.");
	}	
}
