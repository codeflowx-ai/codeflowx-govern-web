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
	name = "SLESCOMPLEXDIALOGUE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCOMPLEXDIALOGUE",
	labelMonitor = "COMPLEX_DIALOGUE",
	pk = "idxslescomplexdialogue" 
)
public class Slescomplexdialogue implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslescomplexdialogue",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslescomplexdialogue;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dialoguetype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String dialoguetype;
	@Column (
		name = "contexthistory",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String contexthistory;
	@Column (
		name = "branchinglogic",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String branchinglogic;
	@Column (
		name = "statemanagement",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String statemanagement;
	@Column (
		name = "intentmapping",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String intentmapping;
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