package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.suinsit.apps.pmo.Pmomtask;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMVERSION" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMVERSION",
	labelMonitor = "",
	pk = "idxpmomversion" 
)
public class Pmomversion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "resumen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String resumen;
	@Column (
		name = "fechaprod",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechaprod;
	@Column (
		name = "fechaprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechaprev;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	@Id
	@Column (
		name = "idxpmomversion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomversion;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomversion" 
	)
	private List<Pmomtask> subpmomtask; 

	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	} 

}