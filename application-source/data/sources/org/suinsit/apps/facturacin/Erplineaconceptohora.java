package org.suinsit.apps.facturacin;

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
import org.suinsit.apps.facturacin.Erprlineahoras;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPLINEACONCEPTOHORA" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPLINEACONCEPTOHORA",
	labelMonitor = "",
	pk = "idxerplineaconceptohora" 
)
public class Erplineaconceptohora implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "concepto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String concepto;
	@Id
	@Column (
		name = "idxerplineaconceptohora",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerplineaconceptohora;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "modulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String modulo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderplineaconceptohora" 
	)
	private List<Erprlineahoras> suberprlineahoras; 

	public List<Erprlineahoras> getSuberprlineahoras() {
		if(this.suberprlineahoras==null)this.suberprlineahoras=new ArrayList<>(0);
		  return this.suberprlineahoras; 
	} 

}