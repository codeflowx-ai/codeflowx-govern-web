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
	name = "SLESRAGOPTIMIZATION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESRAGOPTIMIZATION",
	labelMonitor = "RAG_OPTIMIZATION",
	pk = "idxslesragoptimization" 
)
public class Slesragoptimization implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesragoptimization",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesragoptimization;
	@NotNull
	@NotBlank
	@Column (
		name = "optimizationdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp optimizationdate;
	@Column (
		name = "chunkingconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String chunkingconfig;
	@Column (
		name = "embeddingconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String embeddingconfig;
	@Column (
		name = "retrievalconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String retrievalconfig;
	@Column (
		name = "rerankerconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String rerankerconfig;
	@Column (
		name = "optimizationresults",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String optimizationresults;
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