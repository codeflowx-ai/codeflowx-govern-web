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
import org.suinsit.apps.admin.Mservicio;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MTIPOSERVICIO" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MTIPOSERVICIO",
	labelMonitor = "TIPOSERVICIO",
	pk = "idxmtiposervicio" 
)
public class Mtiposervicio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tiposervicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tiposervicio;
	@Id
	@Column (
		name = "idxmtiposervicio",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmtiposervicio;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmtiposervicio" 
	)
	private List<Mservicio> submservicio; 

	public List<Mservicio> getSubmservicio() {
		if(this.submservicio==null)this.submservicio=new ArrayList<>(0);
		  return this.submservicio; 
	} 

}