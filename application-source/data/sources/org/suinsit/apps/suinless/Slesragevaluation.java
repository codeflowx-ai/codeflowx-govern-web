package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.rag.Ragproyecto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESRAGEVALUATION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESRAGEVALUATION",
	labelMonitor = "RAG_EVALUATION",
	pk = "idxslesragevaluation" 
)
public class Slesragevaluation implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesragevaluation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesragevaluation;
	@NotNull
	@NotBlank
	@Column (
		name = "evaluationdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp evaluationdate;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "contextrelevance",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal contextrelevance;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "answeraccuracy",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal answeraccuracy;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "retrievalquality",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal retrievalquality;
	@Column (
		name = "evaluationdetails",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String evaluationdetails;
	@Column (
		name = "improvements",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String improvements;
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