package org.suinsit.apps.facturacin;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erplineafactprov;
import org.suinsit.apps.facturacin.Erplineafactura;
import org.suinsit.apps.facturacin.Erprlineapedido;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.facturacli.Clicliente;
import org.suinsit.apps.facturacli.Clifactura;
import org.suinsit.apps.facturacli.Cliproducto;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPIMPUESTOS" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPIMPUESTOS",
	labelMonitor = "TIPOIMPUESTO",
	pk = "idxerpimpuestos" 
)
public class Erpimpuestos implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "retencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean retencion;
	@Column (
		name = "activado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activado;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Bienes,Servicios" 
		},
		message = "solamente admite lo valores: ,Bienes,Servicios" 
	)
	@Column (
		name = "ambito",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String ambito;
	@Id
	@Column (
		name = "idxerpimpuestos",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpimpuestos;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "impuesto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String impuesto;
	@Column (
		name = "incluidoprecio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean incluidoprecio;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "iva",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal iva;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Ventas,Compras,Ninguno" 
		},
		message = "solamente admite lo valores: ,Ventas,Compras,Ninguno" 
	)
	@Column (
		name = "tipoimpuesto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipoimpuesto;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCOMPANY0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa idcompany;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Promproducto> subpromproducto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Erprlineapedido> suberprlineapedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Erplineafactprov> suberplineafactprov;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Erplineafactura> suberplineafactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Subscripcion> subsubscripcion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Clifactura> subclifactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Clicliente> subclicliente;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpimpuestos" 
	)
	private List<Cliproducto> subcliproducto; 

	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public List<Promproducto> getSubpromproducto() {
		if(this.subpromproducto==null)this.subpromproducto=new ArrayList<>(0);
		  return this.subpromproducto; 
	}
	
	public List<Erprlineapedido> getSuberprlineapedido() {
		if(this.suberprlineapedido==null)this.suberprlineapedido=new ArrayList<>(0);
		  return this.suberprlineapedido; 
	}
	
	public List<Erplineafactprov> getSuberplineafactprov() {
		if(this.suberplineafactprov==null)this.suberplineafactprov=new ArrayList<>(0);
		  return this.suberplineafactprov; 
	}
	
	public List<Erplineafactura> getSuberplineafactura() {
		if(this.suberplineafactura==null)this.suberplineafactura=new ArrayList<>(0);
		  return this.suberplineafactura; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Subscripcion> getSubsubscripcion() {
		if(this.subsubscripcion==null)this.subsubscripcion=new ArrayList<>(0);
		  return this.subsubscripcion; 
	}
	
	public List<Clifactura> getSubclifactura() {
		if(this.subclifactura==null)this.subclifactura=new ArrayList<>(0);
		  return this.subclifactura; 
	}
	
	public List<Clicliente> getSubclicliente() {
		if(this.subclicliente==null)this.subclicliente=new ArrayList<>(0);
		  return this.subclicliente; 
	}
	
	public List<Cliproducto> getSubcliproducto() {
		if(this.subcliproducto==null)this.subcliproducto=new ArrayList<>(0);
		  return this.subcliproducto; 
	} 

}