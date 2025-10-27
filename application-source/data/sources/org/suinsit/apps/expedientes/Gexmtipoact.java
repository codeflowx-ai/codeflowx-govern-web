package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
import org.suinsit.apps.expedientes.Gexmactuacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXMTIPOACT" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMTIPOACT",
	labelMonitor = "",
	pk = "idxgexmtipoact" 
)
public class Gexmtipoact implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Activo",
		type = "BOOLEAN" 
	)
	private boolean activo;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha Alta",
		type = "DATE" 
	)
	private Date alta;
	@Id
	@Column (
		name = "idxgexmtipoact",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmtipoact;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipoactuacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo de actuación",
		type = "VARCHAR" 
	)
	private String tipoactuacion;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmtipoact" 
	)
	private List<Gexmactuacion> subgexmactuacion; 

	public List<Gexmactuacion> getSubgexmactuacion() {
		if(this.subgexmactuacion==null)this.subgexmactuacion=new ArrayList<>(0);
		  return this.subgexmactuacion; 
	} 

}