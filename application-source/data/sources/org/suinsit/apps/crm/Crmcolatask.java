package org.suinsit.apps.crm;

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
import org.suinsit.apps.crm.Crmtarea;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMCOLATASK" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMCOLATASK",
	labelMonitor = "COLA",
	pk = "idxcrmcolatask" 
)
public class Crmcolatask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cola",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cola;
	@Id
	@Column (
		name = "idxcrmcolatask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmcolatask;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcolatask" 
	)
	private List<Crmtarea> subcrmtarea; 

	public List<Crmtarea> getSubcrmtarea() {
		if(this.subcrmtarea==null)this.subcrmtarea=new ArrayList<>(0);
		  return this.subcrmtarea; 
	} 

}