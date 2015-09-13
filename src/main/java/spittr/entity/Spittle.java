package spittr.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Spittle")
@AttributeOverride(name = "id", column = @Column(name = "SpittleId"))
//@JsonSerialize(using = SpittleSerializer.class)
public class Spittle extends DateByAuditedEntity{
		
	private Spitter spitter;
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
