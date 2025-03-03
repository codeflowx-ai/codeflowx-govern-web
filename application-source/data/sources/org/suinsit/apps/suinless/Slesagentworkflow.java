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
import org.suinsit.apps.suinless.Slesaiagent;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESAGENTWORKFLOW" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAGENTWORKFLOW",
	labelMonitor = "AGENT_WORKFLOW",
	pk = "idxslesagentworkflow" 
)
public class Slesagentworkflow implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesagentworkflow",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesagentworkflow;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "workflowname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String workflowname;
	@Column (
		name = "workflowsteps",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String workflowsteps;
	@Column (
		name = "decisionpoints",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String decisionpoints;
	@Column (
		name = "errorhandling",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String errorhandling;
	@Column (
		name = "metrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String metrics;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDXSLESAIAGENT",
		referencedColumnName = "IDXSLESAIAGENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesaiagent idxslesaiagent; 

	public Slesaiagent getIdxslesaiagent() {
		if(this.idxslesaiagent==null)this.idxslesaiagent=new org.suinsit.apps.suinless.Slesaiagent();
		  return this.idxslesaiagent; 
	} 

}