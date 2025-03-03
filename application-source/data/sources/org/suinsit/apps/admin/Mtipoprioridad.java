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
import org.suinsit.apps.asesor4.Sacticket;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmtarea;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MTIPOPRIORIDAD" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MTIPOPRIORIDAD",
	pk = "idxtipoprioridad" 
)
public class Mtipoprioridad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "baja",
		type = "DATE" 
	)
	private Date baja;
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
		label = "color",
		type = "VARCHAR" 
	)
	private String color;
	@Id
	@Column (
		name = "idxtipoprioridad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtipoprioridad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "prioridad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "prioridad",
		type = "VARCHAR" 
	)
	private String prioridad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipoprioridad" 
	)
	private List<Crmoportunidad> subcrmoportunidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipoprioridad" 
	)
	private List<Sacticket> subsacticket;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipoprioridad" 
	)
	private List<Crmtarea> subcrmtarea; 

	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	}
	
	public List<Sacticket> getSubsacticket() {
		if(this.subsacticket==null)this.subsacticket=new ArrayList<>(0);
		  return this.subsacticket; 
	}
	
	public List<Crmtarea> getSubcrmtarea() {
		if(this.subcrmtarea==null)this.subcrmtarea=new ArrayList<>(0);
		  return this.subcrmtarea; 
	} 

}