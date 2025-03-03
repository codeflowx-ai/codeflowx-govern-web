package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.pmo.Pmomestadotask;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMTIPOTASK" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMTIPOTASK",
	labelMonitor = "",
	pk = "idxpmomtipotask" 
)
public class Pmomtipotask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "resumen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String resumen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipotask",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipotask;
	@Id
	@Column (
		name = "idxpmomtipotask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomtipotask;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomtipotask" 
	)
	private List<Pmomestadotask> subpmomestadotask; 

	public List<Pmomestadotask> getSubpmomestadotask() {
		if(this.subpmomestadotask==null)this.subpmomestadotask=new ArrayList<>(0);
		  return this.subpmomestadotask; 
	} 

}