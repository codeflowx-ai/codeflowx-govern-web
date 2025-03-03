package org.suinsit.apps.sat;

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
import org.suinsit.apps.sat.Satmordenes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATTIPORDEN" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATTIPORDEN",
	labelMonitor = "TIPO",
	pk = "idxsattiporden" 
)
public class Sattiporden implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsattiporden",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsattiporden;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsattiporden" 
	)
	private List<Satmordenes> subsatmordenes; 

	public List<Satmordenes> getSubsatmordenes() {
		if(this.subsatmordenes==null)this.subsatmordenes=new ArrayList<>(0);
		  return this.subsatmordenes; 
	} 

}