package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.rag.Ragdocumento;
import org.suinsit.apps.rag.Ragproyecto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RAGDATASOURCE" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGDATASOURCE",
	labelMonitor = "RAG_DATASOURCE",
	pk = "idxragdatasource" 
)
public class Ragdatasource implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "configuracion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String configuracion;
	@Column (
		name = "credenciales",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String credenciales;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String estado;
	@Id
	@Column (
		name = "idxragdatasource",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragdatasource;
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
	@Column (
		name = "ultimasincronizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date ultimasincronizacion;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxragdatasource" 
	)
	private List<Ragdocumento> subragdocumento; 

	public Ragproyecto getIdragproyecto() {
		if(this.idragproyecto==null)this.idragproyecto=new org.suinsit.apps.rag.Ragproyecto();
		  return this.idragproyecto; 
	}
	
	public List<Ragdocumento> getSubragdocumento() {
		if(this.subragdocumento==null)this.subragdocumento=new ArrayList<>(0);
		  return this.subragdocumento; 
	} 

}