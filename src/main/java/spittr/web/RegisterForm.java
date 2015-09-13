package spittr.web;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import spittr.entity.Spitter;

public class RegisterForm {
	
	@NotNull
    @Size(min=5, max=16, message="{username.size}")    
	private String username;
	
    @NotNull
    @Size(min=5, max=16, message="{password.size}")
	private String password;
    
    @NotNull
    @Size(min=5, max=16, message="{password.size}")
	private String passwordConfirm;
    
	private String fullName;
		  
	@NotNull
	@Email
	private String email;
	
	private boolean updateByEmail;
		
  	public String getFullName() {
	  	return fullName;
  	}
  
  	public void setFullName(String fullName) {
  		this.fullName = fullName;
  	}
  
  	public String getUsername() {
	  	return username;
  	}
  
  	public void setUsername(String username) {
	  	this.username = username;
  	}
  
  	public String getPassword() {
	  	return password;
  	}
  
  	public void setPassword(String password) {
	  	this.password = password;
  	}
  
  	public String getPasswordConfirm() {
	  	return passwordConfirm;
    }
  
    public void setPasswordConfirm(String passwordConfirm) {
	  	this.passwordConfirm = passwordConfirm;
    }
    
    @AssertTrue(message="Passwords should match")
	private boolean isValid() {
	    return this.password.equals(this.passwordConfirm);
	}
    
  	public String getEmail() {
  		return email;
  	}
  
  	public void setEmail(String email) {
  		this.email = email;
    }
  
  	public boolean isUpdateByEmail() {
		return updateByEmail;
	}

	public void setUpdateByEmail(boolean updateByEmail) {
		this.updateByEmail = updateByEmail;
	}
  
	public Spitter toSpitter() {
		Spitter spitter = new Spitter();
		spitter.setUsername(username);
		spitter.setPassword(password);
		spitter.setFullName(fullName);
		spitter.setEmail(email);
		spitter.setUpdateByEmail(updateByEmail);
		return spitter;
	}
}