package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.expedientes.Gexmepediente;
import org.suinsit.apps.subvenciones.Submorganismo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXMTIPO" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMTIPO",
	labelMonitor = "tipo",
	pk = "idxgexmtipo" 
)
public class Gexmtipo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "duracion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer duracion;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Id
	@Column (
		name = "idxgexmtipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmtipo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBMORGANISMO0",
		referencedColumnName = "IDXSUBMORGANISMO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Submorganismo idsubmorganismo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmtipo" 
	)
	private List<Gexmepediente> subgexmepediente; 

	public Submorganismo getIdsubmorganismo() {
		if(this.idsubmorganismo==null)this.idsubmorganismo=new org.suinsit.apps.subvenciones.Submorganismo();
		  return this.idsubmorganismo; 
	}
	
	public List<Gexmepediente> getSubgexmepediente() {
		if(this.subgexmepediente==null)this.subgexmepediente=new ArrayList<>(0);
		  return this.subgexmepediente; 
	} 

}