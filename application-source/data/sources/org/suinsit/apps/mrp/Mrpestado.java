package org.suinsit.apps.mrp;

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
import org.suinsit.apps.mrp.Mrpexpedicion;
import org.suinsit.apps.mrp.Mrpmrecepcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPESTADO" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPESTADO",
	labelMonitor = "ESTADO",
	pk = "idxmrpestado" 
)
public class Mrpestado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",primary,secundary,warning,dark,danger,sucess" 
		},
		message = "solamente admite lo valores: ,primary,secundary,warning,dark,danger,sucess" 
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
		name = "idxmrpestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpestado;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "module",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String module;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpestado" 
	)
	private List<Mrpmrecepcion> submrpmrecepcion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpestado" 
	)
	private List<Mrpexpedicion> submrpexpedicion; 

	public List<Mrpmrecepcion> getSubmrpmrecepcion() {
		if(this.submrpmrecepcion==null)this.submrpmrecepcion=new ArrayList<>(0);
		  return this.submrpmrecepcion; 
	}
	
	public List<Mrpexpedicion> getSubmrpexpedicion() {
		if(this.submrpexpedicion==null)this.submrpexpedicion=new ArrayList<>(0);
		  return this.submrpexpedicion; 
	} 

}