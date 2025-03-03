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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.sat.Satmordenes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATMESTADO" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATMESTADO",
	labelMonitor = "ESTADO",
	pk = "idxsatmestado" 
)
public class Satmestado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",BG_PRMARY,BG_DARK,BG_SUCCESS,BG_DANGER" 
		},
		message = "solamente admite lo valores: ,BG_PRMARY,BG_DARK,BG_SUCCESS,BG_DANGER" 
	)
	@Column (
		name = "bgcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String bgcolor;
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
		name = "idxsatmestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsatmestado;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsatmestado" 
	)
	private List<Satmordenes> subsatmordenes; 

	public List<Satmordenes> getSubsatmordenes() {
		if(this.subsatmordenes==null)this.subsatmordenes=new ArrayList<>(0);
		  return this.subsatmordenes; 
	} 

}