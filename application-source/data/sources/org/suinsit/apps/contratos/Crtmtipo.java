package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Mperidicidad;
import org.suinsit.apps.contratos.Crtmcontrato;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRTMTIPO" 
)
@Entidad (
	namespace = "contratos",
	type = "TABLE",
	name = "CRTMTIPO",
	labelMonitor = "tipocontrato",
	pk = "idxcrtmtipo" 
)
public class Crtmtipo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal precio;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activo;
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
		name = "idxcrtmtipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrtmtipo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipocontrato",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipocontrato;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPERIDICIDAD0",
		referencedColumnName = "IDXMPERIDICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mperidicidad idmperidicidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmtipo" 
	)
	private List<Crtmcontrato> subcrtmcontrato; 

	public Mperidicidad getIdmperidicidad() {
		if(this.idmperidicidad==null)this.idmperidicidad=new org.suinsit.apps.admin.Mperidicidad();
		  return this.idmperidicidad; 
	}
	
	public List<Crtmcontrato> getSubcrtmcontrato() {
		if(this.subcrtmcontrato==null)this.subcrtmcontrato=new ArrayList<>(0);
		  return this.subcrtmcontrato; 
	} 

}