package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.rag.Ragdatasource;
import org.suinsit.apps.rag.Ragproyecto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RAGDOCUMENTO" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGDOCUMENTO",
	labelMonitor = "RAG_DOCUMENTO",
	pk = "idxragdocumento" 
)
public class Ragdocumento implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxragdocumento",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragdocumento;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String nombre;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LIST_STRING" 
	)
	private List tipo;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "estado",
		nullable = true 
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
		max = 100 
	)
	@Column (
		name = "hash",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String hash;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ruta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String ruta;
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
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LONG" 
	)
	private Long version;
	@Column (
		name = "contenido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "CLOB" 
	)
	private String contenido;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "idxragproyecto",
		referencedColumnName = "idxragproyecto",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragproyecto idxragproyecto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "idxragdatasource",
		referencedColumnName = "idxragdatasource",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragdatasource idxragdatasource; 

	public Ragproyecto getIdxragproyecto() {
		if(this.idxragproyecto==null)this.idxragproyecto=new org.suinsit.apps.rag.Ragproyecto();
		  return this.idxragproyecto; 
	}
	
	public Ragdatasource getIdxragdatasource() {
		if(this.idxragdatasource==null)this.idxragdatasource=new org.suinsit.apps.rag.Ragdatasource();
		  return this.idxragdatasource; 
	} 

}