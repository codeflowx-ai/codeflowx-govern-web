package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
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
	name = "SLESAGENTINTERACTION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAGENTINTERACTION",
	labelMonitor = "AGENT_INTERACTION",
	pk = "idxslesagentinteraction" 
)
public class Slesagentinteraction implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesagentinteraction",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesagentinteraction;
	@NotNull
	@NotBlank
	@Column (
		name = "interactiondate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp interactiondate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "interactiontype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String interactiontype;
	@Column (
		name = "inputdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String inputdata;
	@Column (
		name = "outputdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String outputdata;
	@Column (
		name = "context",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String context;
	@Column (
		name = "performance",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String performance;
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