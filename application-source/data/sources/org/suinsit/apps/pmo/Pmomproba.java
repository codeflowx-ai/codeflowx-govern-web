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
import org.suinsit.apps.pmo.Pmoriesgo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMPROBA" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMPROBA",
	labelMonitor = "",
	pk = "idxpmomproba" 
)
public class Pmomproba implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "probabilidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String probabilidad;
	@Id
	@Column (
		name = "idxpmomproba",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomproba;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproba" 
	)
	private List<Pmoriesgo> subpmoriesgo; 

	public List<Pmoriesgo> getSubpmoriesgo() {
		if(this.subpmoriesgo==null)this.subpmoriesgo=new ArrayList<>(0);
		  return this.subpmoriesgo; 
	} 

}