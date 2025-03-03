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
import org.suinsit.apps.crm.Crmropocontact;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MROLCARGO" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MROLCARGO",
	pk = "idxmrolcargo" 
)
public class Mrolcargo implements Serializable { 

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
		type = "BOOLEAN" 
	)
	private boolean baja;
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
		name = "idxmrolcargo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrolcargo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrolcargo" 
	)
	private List<Crmropocontact> subcrmropocontact; 

	public List<Crmropocontact> getSubcrmropocontact() {
		if(this.subcrmropocontact==null)this.subcrmropocontact=new ArrayList<>(0);
		  return this.subcrmropocontact; 
	} 

}