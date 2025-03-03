package org.suinsit.apps.admin;

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
import org.suinsit.apps.crm.Crmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MTIPOOPORTUNIDAD" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MTIPOOPORTUNIDAD",
	pk = "idxtipooportunidad" 
)
public class Mtipooportunidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipooportunidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipooportunidad;
	@Id
	@Column (
		name = "idxtipooportunidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtipooportunidad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipooportunidad" 
	)
	private List<Crmoportunidad> subcrmoportunidad; 

	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	} 

}