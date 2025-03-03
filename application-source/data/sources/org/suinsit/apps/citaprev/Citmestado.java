package org.suinsit.apps.citaprev;

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
import org.suinsit.apps.citaprev.Citmagenda;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITMESTADO" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITMESTADO",
	labelMonitor = "ESTADO",
	pk = "idxcitmestado" 
)
public class Citmestado implements Serializable { 

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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estado;
	@Id
	@Column (
		name = "idxcitmestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcitmestado;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcitmestado" 
	)
	private List<Citmagenda> subcitmagenda; 

	public List<Citmagenda> getSubcitmagenda() {
		if(this.subcitmagenda==null)this.subcitmagenda=new ArrayList<>(0);
		  return this.subcitmagenda; 
	} 

}