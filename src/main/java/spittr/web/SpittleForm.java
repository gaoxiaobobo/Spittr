package spittr.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SpittleForm {

	@NotNull
	@Size(min = 2, max = 140, message = "{validate.spittle.message}")
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
