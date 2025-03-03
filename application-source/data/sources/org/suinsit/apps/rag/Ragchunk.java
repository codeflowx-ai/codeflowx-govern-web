package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.suinsit.apps.rag.Ragdocumento;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RAGCHUNK" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGCHUNK",
	labelMonitor = "RAG_CHUNK",
	pk = "idxragchunk" 
)
public class Ragchunk implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "contenido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TEXT" 
	)
	private String contenido;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "embedding",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "VECTOR" 
	)
	private float[] embedding;
	@Column (
		name = "fechacreacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechacreacion;
	@Column (
		name = "fechamodificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechamodificacion;
	@Column (
		name = "fin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "INTEGER" 
	)
	private Integer fin;
	@Id
	@Column (
		name = "idxragchunk",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragchunk;
	@Column (
		name = "inicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "INTEGER" 
	)
	private Integer inicio;
	@Column (
		name = "metadata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String metadata;
	@Column (
		name = "searchvector",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "TSVECTOR" 
	)
	private String searchvector;
	@Column (
		name = "searchvectoren",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "TSVECTOR" 
	)
	private String searchvectoren;
	@Column (
		name = "tokens",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "INTEGER" 
	)
	private Integer tokens;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRAGDOCUMENTO0",
		referencedColumnName = "IDXRAGDOCUMENTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragdocumento idragdocumento; 

	public Ragdocumento getIdragdocumento() {
		if(this.idragdocumento==null)this.idragdocumento=new org.suinsit.apps.rag.Ragdocumento();
		  return this.idragdocumento; 
	} 

}