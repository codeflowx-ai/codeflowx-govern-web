package org.suinsit.apps.pmo;

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
import org.suinsit.apps.pmo.Pmoproceso;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMACTIVIDAD" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMACTIVIDAD",
	labelMonitor = "",
	pk = "idxpmomactividad" 
)
public class Pmomactividad implements Serializable { 

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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "actividad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String actividad;
	@Id
	@Column (
		name = "idxpmomactividad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomactividad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomactividad" 
	)
	private List<Pmoproceso> subpmoproceso; 

	public List<Pmoproceso> getSubpmoproceso() {
		if(this.subpmoproceso==null)this.subpmoproceso=new ArrayList<>(0);
		  return this.subpmoproceso; 
	} 

}