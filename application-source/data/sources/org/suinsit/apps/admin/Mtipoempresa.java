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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.crm.Crmempresa;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MTIPOEMPRESA" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MTIPOEMPRESA",
	pk = "idxmtipoempresa" 
)
public class Mtipoempresa implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmtipoempresa",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmtipoempresa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipoempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "tipoempresa",
		type = "VARCHAR" 
	)
	private String tipoempresa;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtipoempresa" 
	)
	private List<Crmempresa> subcrmempresa; 

	public List<Crmempresa> getSubcrmempresa() {
		if(this.subcrmempresa==null)this.subcrmempresa=new ArrayList<>(0);
		  return this.subcrmempresa; 
	} 

}