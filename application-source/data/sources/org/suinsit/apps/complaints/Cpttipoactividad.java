package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.complaints.Cptactividad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTTIPOACTIVIDAD" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTTIPOACTIVIDAD",
	labelMonitor = "TIPOACTIVIDAD",
	pk = "idxcpttipoactividad" 
)
public class Cpttipoactividad implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "idxcpttipoactividad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcpttipoactividad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipoactividad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipoactividad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcpttipoactividad" 
	)
	private List<Cptactividad> subcptactividad; 

	public List<Cptactividad> getSubcptactividad() {
		if(this.subcptactividad==null)this.subcptactividad=new ArrayList<>(0);
		  return this.subcptactividad; 
	} 

}