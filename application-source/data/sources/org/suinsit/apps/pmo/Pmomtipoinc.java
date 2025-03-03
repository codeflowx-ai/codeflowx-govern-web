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
import org.suinsit.apps.pmo.Pmomincidencia;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMTIPOINC" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMTIPOINC",
	labelMonitor = "",
	pk = "idxpmomtipoinc" 
)
public class Pmomtipoinc implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxpmomtipoinc",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomtipoinc;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomtipoinc" 
	)
	private List<Pmomincidencia> subpmomincidencia; 

	public List<Pmomincidencia> getSubpmomincidencia() {
		if(this.subpmomincidencia==null)this.subpmomincidencia=new ArrayList<>(0);
		  return this.subpmomincidencia; 
	} 

}