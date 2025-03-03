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
	name = "PMOAPROBACION" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOAPROBACION",
	pk = "idxpmoaprobacion" 
)
public class Pmoaprobacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "aprobado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean aprobado;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "aprobacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String aprobacion;
	@Id
	@Column (
		name = "idxpmoaprobacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoaprobacion;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoaprobacion" 
	)
	private List<Pmotipoproyect> subpmotipoproyect;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoaprobacion" 
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