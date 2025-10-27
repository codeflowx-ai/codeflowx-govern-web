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
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmotipoproyect;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOPRIORIDAD" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOPRIORIDAD",
	pk = "idxpmoprioridad" 
)
public class Pmoprioridad implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		label = "",
		type = "VARCHAR" 
	)
	private String prioridad;
	@Id
	@Column (
		name = "idxpmoprioridad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoprioridad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoprioridad" 
	)
	private List<Pmotipoproyect> subpmotipoproyect;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoprioridad" 
	)
	private List<Pmomproject> subpmomproject; 

	public List<Pmotipoproyect> getSubpmotipoproyect() {
		if(this.subpmotipoproyect==null)this.subpmotipoproyect=new ArrayList<>(0);
		  return this.subpmotipoproyect; 
	}
	
	public List<Pmomproject> getSubpmomproject() {
		if(this.subpmomproject==null)this.subpmomproject=new ArrayList<>(0);
		  return this.subpmomproject; 
	} 

}