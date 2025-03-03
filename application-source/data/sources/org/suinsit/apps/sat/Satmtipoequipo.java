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
import org.suinsit.apps.sat.Satmequipocli;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATMTIPOEQUIPO" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATMTIPOEQUIPO",
	labelMonitor = "TIPO",
	pk = "idxsatmtipoequipo" 
)
public class Satmtipoequipo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Id
	@Column (
		name = "idxsatmtipoequipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsatmtipoequipo;
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
		mappedBy = "idsatmtipoequipo" 
	)
	private List<Satmequipocli> subsatmequipocli; 

	public List<Satmequipocli> getSubsatmequipocli() {
		if(this.subsatmequipocli==null)this.subsatmequipocli=new ArrayList<>(0);
		  return this.subsatmequipocli; 
	} 

}