package org.suinsit.apps.facturacli;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.facturacli.Clifactura;
import org.suinsit.apps.facturacli.Cliproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CLILINEAFACTURA" 
)
@Entidad (
	namespace = "facturacli",
	type = "TABLE",
	name = "CLILINEAFACTURA",
	labelMonitor = "",
	pk = "idxclilineafactura" 
)
public class Clilineafactura implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Column (
		name = "endperiodo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date endperiodo;
	@Id
	@Column (
		name = "idxclilineafactura",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxclilineafactura;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impuesto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impuesto;
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
		name = "startperiodo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date startperiodo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "subtotal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal subtotal;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLIFACTURA0",
		referencedColumnName = "IDXCLIFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Clifactura idclifactura;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLIPRODUCTO0",
		referencedColumnName = "IDXCLIPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cliproducto idcliproducto; 

	public Clifactura getIdclifactura() {
		if(this.idclifactura==null)this.idclifactura=new org.suinsit.apps.facturacli.Clifactura();
		  return this.idclifactura; 
	}
	
	public Cliproducto getIdcliproducto() {
		if(this.idcliproducto==null)this.idcliproducto=new org.suinsit.apps.facturacli.Cliproducto();
		  return this.idcliproducto; 
	} 

}