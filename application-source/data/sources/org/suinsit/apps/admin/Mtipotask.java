package org.suinsit.apps.admin;

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
import org.suinsit.apps.crm.Crmtarea;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MTIPOTASK" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MTIPOTASK",
	labelMonitor = "",
	pk = "idxtipotask" 
)
public class Mtipotask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "color",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String color;
	@Id
	@Column (
		name = "idxtipotask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtipotask;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipotarea",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo de Tarea",
		type = "VARCHAR" 
	)
	private String tipotarea;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipotask" 
	)
	private List<Crmtarea> subcrmtarea; 

	public List<Crmtarea> getSubcrmtarea() {
		if(this.subcrmtarea==null)this.subcrmtarea=new ArrayList<>(0);
		  return this.subcrmtarea; 
	} 

}