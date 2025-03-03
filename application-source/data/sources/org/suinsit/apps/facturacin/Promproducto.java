package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.facturacin.Erpperiodicidad;
import org.suinsit.apps.facturacin.Promcategoria;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PROMPRODUCTO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "PROMPRODUCTO",
	labelMonitor = "producto",
	pk = "idxpromproducto" 
)
public class Promproducto implements Serializable { 

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
		name = "codbarras",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codbarras;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codsku",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codsku;
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
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",unidades entregadas,Unidades vendidas" 
		},
		message = "solamente admite lo valores: ,unidades entregadas,Unidades vendidas" 
	)
	@Column (
		name = "facturacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo de Facturación",
		type = "ENUM_STRING" 
	)
	private String facturacion;
	@Id
	@Column (
		name = "idxpromproducto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpromproducto;
	@Column (
		name = "nota",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String nota;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "peso",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal peso;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "preciocoste",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Precio de coste",
		type = "DECIMAL" 
	)
	private BigDecimal preciocoste;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precioventa",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Precio de venta",
		type = "DECIMAL" 
	)
	private BigDecimal precioventa;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "producto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Producto",
		type = "VARCHAR" 
	)
	private String producto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refinterna",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refinterna;
	@Column (
		name = "servicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean servicio;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "stock",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal stock;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "stockmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal stockmin;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Producto,Servicio,Consumible" 
		},
		message = "solamente admite lo valores: ,Producto,Servicio,Consumible" 
	)
	@Column (
		name = "tipoprod",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo Producto",
		type = "ENUM_STRING" 
	)
	private String tipoprod;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMCATEGORIA0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idpromcategoria;
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
		name = "IDERPPERIODICIDAD0",
		referencedColumnName = "IDXERPPERIODICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpperiodicidad iderpperiodicidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROVEEDOR0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idproveedor;
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

	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	}
	
	public Erpimpuestos getIderpimpuestos() {
		if(this.iderpimpuestos==null)this.iderpimpuestos=new org.suinsit.apps.facturacin.Erpimpuestos();
		  return this.iderpimpuestos; 
	}
	
	public Erpperiodicidad getIderpperiodicidad() {
		if(this.iderpperiodicidad==null)this.iderpperiodicidad=new org.suinsit.apps.facturacin.Erpperiodicidad();
		  return this.iderpperiodicidad; 
	}
	
	public Crmempresa getIdproveedor() {
		if(this.idproveedor==null)this.idproveedor=new org.suinsit.apps.crm.Crmempresa();
		  return this.idproveedor; 
	}
	
	public Erpmproveedor getIderpmproveedor() {
		if(this.iderpmproveedor==null)this.iderpmproveedor=new org.suinsit.apps.facturacin.Erpmproveedor();
		  return this.iderpmproveedor; 
	} 

}