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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.rag.Ragproyecto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESRAGMETRICS" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESRAGMETRICS",
	labelMonitor = "RAG_METRICS",
	pk = "idxslesragmetrics" 
)
public class Slesragmetrics implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesragmetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesragmetrics;
	@NotNull
	@NotBlank
	@Column (
		name = "metricdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp metricdate;
	@Column (
		name = "retrievalmetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String retrievalmetrics;
	@Column (
		name = "generationmetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String generationmetrics;
	@Column (
		name = "performancemetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String performancemetrics;
	@Column (
		name = "qualitymetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String qualitymetrics;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESRAGPROJECT",
		referencedColumnName = "IDXSLESRAGPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragproyecto idslesragproject; 

	public Ragproyecto getIdslesragproject() {
		if(this.idslesragproject==null)this.idslesragproject=new org.suinsit.apps.rag.Ragproyecto();
		  return this.idslesragproject; 
	} 

}