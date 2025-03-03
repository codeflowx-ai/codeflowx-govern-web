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
	name = "SLESCHATFLOWTEMPLATE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATFLOWTEMPLATE",
	labelMonitor = "CHAT_FLOW_TEMPLATE",
	pk = "idxsleschatflowtemplate" 
)
public class Sleschatflowtemplate implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatflowtemplate",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatflowtemplate;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "flowname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String flowname;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "businessarea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String businessarea;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "flowtype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String flowtype;
	@Column (
		name = "promptsequence",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String promptsequence;
	@Column (
		name = "variables",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String variables;
	@Column (
		name = "decisionrules",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String decisionrules;
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