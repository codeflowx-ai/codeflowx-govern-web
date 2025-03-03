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
import org.suinsit.apps.mrp.Mrpmtipoperacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPMTIPOOPERA" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPMTIPOOPERA",
	labelMonitor = "TIPOOPERA",
	pk = "idxmrpmtipoopera" 
)
public class Mrpmtipoopera implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmrpmtipoopera",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpmtipoopera;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipoopera",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipoopera;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpmtipoopera" 
	)
	private List<Mrpmtipoperacion> submrpmtipoperacion; 

	public List<Mrpmtipoperacion> getSubmrpmtipoperacion() {
		if(this.submrpmtipoperacion==null)this.submrpmtipoperacion=new ArrayList<>(0);
		  return this.submrpmtipoperacion; 
	} 

}