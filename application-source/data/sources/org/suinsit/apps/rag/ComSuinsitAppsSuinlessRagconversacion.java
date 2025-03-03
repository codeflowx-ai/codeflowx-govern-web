package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	name = "RAGCONVERSACION" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGCONVERSACION",
	labelMonitor = "RAG_CONVERSACION",
	pk = "idxragconversacion" 
)
public class ComSuinsitAppsSuinlessRagconversacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxragconversacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragconversacion;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "titulo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String titulo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String descripcion;
	@Column (
		name = "fechainicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechainicio;
	@Column (
		name = "fechafin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechafin;
	@Column (
		name = "ultimaactividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date ultimaactividad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 200 
	)
	@Column (
		name = "estado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LIST_STRING" 
	)
	private List estado;
	@Size (
		min = 0,
		max = 200 
	)
	@Column (
		name = "modelo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LIST_STRING" 
	)
	private List modelo;
	@Column (
		name = "contexto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String contexto;
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
		name = "parametros",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String parametros;
	@Column (
		name = "tokensutilizados",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LONG" 
	)
	private Long tokensutilizados;
	@Column (
		name = "costecalculado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal costecalculado;
	@Column (
		name = "tiemporespuesta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LONG" 
	)
	private Long tiemporespuesta;
	@Column (
		name = "valoracionusuario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "INTEGER" 
	)
	private Integer valoracionusuario;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "feedbackusuario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String feedbackusuario;
	@Column (
		name = "documentosreferenciados",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String documentosreferenciados;
	@Column (
		name = "tags",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TEXT_ARRAY" 
	)
	private List tags;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idioma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String idioma;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sessionid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String sessionid;
	private boolean updatable; 

}