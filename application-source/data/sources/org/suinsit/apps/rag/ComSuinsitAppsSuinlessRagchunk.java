package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
	name = "RAGCHUNK" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGCHUNK",
	labelMonitor = "RAG_CHUNK",
	pk = "idxragchunk" 
)
public class ComSuinsitAppsSuinlessRagchunk implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "search_vector",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "TSVECTOR" 
	)
	private String search_vector;
	@Column (
		name = "search_vector_en",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "TSVECTOR" 
	)
	private String search_vector_en;
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
	private boolean updatable; 

}