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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.pmo.Pmomproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOPATROCINADOR" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOPATROCINADOR",
	labelMonitor = "",
	pk = "idxpmopatrocinador" 
)
public class Pmopatrocinador implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "patrocinador",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String patrocinador;
	@Id
	@Column (
		name = "idxpmopatrocinador",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmopatrocinador;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmopatrocinador" 
	)
	private List<Pmomproject> subpmomproject; 

	public List<Pmomproject> getSubpmomproject() {
		if(this.subpmomproject==null)this.subpmomproject=new ArrayList<>(0);
		  return this.subpmomproject; 
	} 

}