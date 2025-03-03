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
import org.suinsit.apps.suinless.Slesagent;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESAGENTTASK" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAGENTTASK",
	labelMonitor = "AGENT_TASK",
	pk = "idxslesagenttask" 
)
public class Slesagenttask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesagenttask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesagenttask;
	@NotNull
	@NotBlank
	@Column (
		name = "taskdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp taskdate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tasktype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String tasktype;
	@Column (
		name = "taskdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String taskdata;
	@Column (
		name = "result",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String result;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String status;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESAGENT",
		referencedColumnName = "IDXSLESAGENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesagent idslesagent; 

	public Slesagent getIdslesagent() {
		if(this.idslesagent==null)this.idslesagent=new org.suinsit.apps.suinless.Slesagent();
		  return this.idslesagent; 
	} 

}