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
import org.suinsit.apps.crm.Crmcontacto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MCARGO" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MCARGO",
	pk = "idxmcargo" 
)
public class Mcargo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cargo;
	@Id
	@Column (
		name = "idxmcargo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmcargo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmcargo" 
	)
	private List<Crmcontacto> subcrmcontacto; 

	public List<Crmcontacto> getSubcrmcontacto() {
		if(this.subcrmcontacto==null)this.subcrmcontacto=new ArrayList<>(0);
		  return this.subcrmcontacto; 
	} 

}