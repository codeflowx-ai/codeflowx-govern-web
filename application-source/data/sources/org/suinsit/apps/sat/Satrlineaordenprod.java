package org.suinsit.apps.sat;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.sat.Satmordenes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATRLINEAORDENPROD" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATRLINEAORDENPROD",
	labelMonitor = "",
	pk = "idxsatrlineaordenprod" 
)
public class Satrlineaordenprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "vatpercent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal vatpercent;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "seccion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String seccion;
	@Size (
		min = 0,
		max = 500 
	)
	@Column (
		name = "nota",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nota;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "descuento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal descuento;
	@Size (
		min = 0,
		max = 500 
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
	@Id
	@Column (
		name = "idxsatrlineaordenprod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsatrlineaordenprod;
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
		name = "IDSATMORDENES0",
		referencedColumnName = "IDXSATMORDENES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Satmordenes idsatmordenes;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMPRODUCTO0",
		referencedColumnName = "IDXPROMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promproducto idpromproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPIMPUESTOS0",
		referencedColumnName = "IDXERPIMPUESTOS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpimpuestos iderpimpuestos;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMPROVEEDOR0",
		referencedColumnName = "IDXERPMPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmproveedor iderpmproveedor; 

	public Satmordenes getIdsatmordenes() {
		if(this.idsatmordenes==null)this.idsatmordenes=new org.suinsit.apps.sat.Satmordenes();
		  return this.idsatmordenes; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Erpimpuestos getIderpimpuestos() {
		if(this.iderpimpuestos==null)this.iderpimpuestos=new org.suinsit.apps.facturacin.Erpimpuestos();
		  return this.iderpimpuestos; 
	}
	
	public Erpmproveedor getIderpmproveedor() {
		if(this.iderpmproveedor==null)this.iderpmproveedor=new org.suinsit.apps.facturacin.Erpmproveedor();
		  return this.iderpmproveedor; 
	} 

}