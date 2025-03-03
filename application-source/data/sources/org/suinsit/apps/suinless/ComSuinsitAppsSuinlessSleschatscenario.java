package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Sleschatbot;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHATSCENARIO" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATSCENARIO",
	labelMonitor = "CHAT_SCENARIO",
	pk = "idxsleschatscenario" 
)
public class ComSuinsitAppsSuinlessSleschatscenario implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatscenario",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxsleschatscenario;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "scenarioname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String scenarioname;
	@Column (
		name = "complexity",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String complexity;
	@Column (
		name = "scenarioconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String scenarioconfig;
	@Column (
		name = "decisiontrees",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String decisiontrees;
	@Column (
		name = "fallbackstrategies",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String fallbackstrategies;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDXSLESCHATBOT",
		referencedColumnName = "IDXSLESCHATBOT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sleschatbot idxsleschatbot; 

	public Sleschatbot getIdxsleschatbot() {
		if(this.idxsleschatbot==null)this.idxsleschatbot=new org.suinsit.apps.suinless.Sleschatbot();
		  return this.idxsleschatbot; 
	} 

}