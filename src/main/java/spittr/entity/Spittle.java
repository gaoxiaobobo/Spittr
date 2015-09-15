package spittr.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Spittle")
@AttributeOverride(name = "id", column = @Column(name = "SpittleId"))
//@JsonSerialize(using = SpittleSerializer.class)
public class Spittle extends DateByAuditedEntity{
	
	@NotNull
	private Spitter spitter;
	
	@Size(min = 2, max = 140, message = "{validate.spittle.message}")
	private String message;	
		
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "SpitterId")
	public Spitter getSpitter() {
		return spitter;
	}

	public void setSpitter(Spitter spitter) {
		this.spitter = spitter;
	}

	@Basic
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
