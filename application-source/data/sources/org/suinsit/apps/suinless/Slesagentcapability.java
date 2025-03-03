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
	name = "SLESAGENTCAPABILITY" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAGENTCAPABILITY",
	labelMonitor = "AGENT_CAPABILITY",
	pk = "idxslesagentcapability" 
)
public class Slesagentcapability implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesagentcapability",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesagentcapability;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "capabilityname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String capabilityname;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "capabilitytype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String capabilitytype;
	@Column (
		name = "implementation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String implementation;
	@Column (
		name = "parameters",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String parameters;
	@Column (
		name = "dependencies",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String dependencies;
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