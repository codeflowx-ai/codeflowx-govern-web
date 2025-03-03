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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.rag.Ragproyecto;

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
public class Ragconversacion implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Size (
		min = 0,
		max = 16 
	)
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 1535 
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
	@Size (
		min = 0,
		max = 1535 
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
	@Size (
		min = 0,
		max = 1535 
	)
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
		max = 16 
	)
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
		max = 16 
	)
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRAGPROYECTO0",
		referencedColumnName = "IDXRAGPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragproyecto idragproyecto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario; 

	public Ragproyecto getIdragproyecto() {
		if(this.idragproyecto==null)this.idragproyecto=new org.suinsit.apps.rag.Ragproyecto();
		  return this.idragproyecto; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}