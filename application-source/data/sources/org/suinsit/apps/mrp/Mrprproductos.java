package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.mrp.Mrpexpedicion;
import org.suinsit.apps.mrp.Mrpmrecepcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPRPRODUCTOS" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPRPRODUCTOS",
	labelMonitor = "",
	pk = "idxmrprproductos" 
)
public class Mrprproductos implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "realizado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal realizado;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "unidades",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal unidades;
	@Id
	@Column (
		name = "idxmrprproductos",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrprproductos;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPMRECEPCION0",
		referencedColumnName = "IDXMRPMINVENTARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpmrecepcion idmrpmrecepcion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPEXPEDICION0",
		referencedColumnName = "IDXMRPEXPEDICION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpexpedicion idmrpexpedicion; 

	public Mrpmrecepcion getIdmrpmrecepcion() {
		if(this.idmrpmrecepcion==null)this.idmrpmrecepcion=new org.suinsit.apps.mrp.Mrpmrecepcion();
		  return this.idmrpmrecepcion; 
	}
	
	public Mrpexpedicion getIdmrpexpedicion() {
		if(this.idmrpexpedicion==null)this.idmrpexpedicion=new org.suinsit.apps.mrp.Mrpexpedicion();
		  return this.idmrpexpedicion; 
	} 

}