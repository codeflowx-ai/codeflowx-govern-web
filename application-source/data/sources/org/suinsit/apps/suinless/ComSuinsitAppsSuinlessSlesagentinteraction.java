package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

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
public class ComSuinsitAppsSuinlessSlesagentinteraction implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesagentinteraction",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
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

}