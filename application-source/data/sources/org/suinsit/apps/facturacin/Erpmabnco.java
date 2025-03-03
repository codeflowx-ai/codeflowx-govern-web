package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.FieldType;
import org.suinsit.apps.facturacin.Ctbcbrofact;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpfactcompra;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpfacturae;
import org.suinsit.apps.facturacin.Erpfactvenci;
import org.suinsit.apps.facturacin.Erpmgasto;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erppresupuesto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMABNCO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMABNCO",
	labelMonitor = "BANCO",
	pk = "idxerpmabnco" 
)
public class Erpmabnco implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "banco",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String banco;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codiban",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.IBAN 
	)
	private String codiban;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codswift",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codswift;
	@Id
	@Column (
		name = "idxerpmabnco",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmabnco;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPEMPRESA0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa iderpempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpfactcompra> suberpfactcompra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Ctbcbrofact> subctbcbrofact;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpfactvenci> suberpfactvenci;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpmgasto> suberpmgasto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpmproveedor> suberpmproveedor;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erppedido> suberppedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmabnco" 
	)
	private List<Erpfacturae> suberpfacturae; 

	public Erpempresa getIderpempresa() {
		if(this.iderpempresa==null)this.iderpempresa=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.iderpempresa; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erpfactcompra> getSuberpfactcompra() {
		if(this.suberpfactcompra==null)this.suberpfactcompra=new ArrayList<>(0);
		  return this.suberpfactcompra; 
	}
	
	public List<Ctbcbrofact> getSubctbcbrofact() {
		if(this.subctbcbrofact==null)this.subctbcbrofact=new ArrayList<>(0);
		  return this.subctbcbrofact; 
	}
	
	public List<Erpfactvenci> getSuberpfactvenci() {
		if(this.suberpfactvenci==null)this.suberpfactvenci=new ArrayList<>(0);
		  return this.suberpfactvenci; 
	}
	
	public List<Erpmgasto> getSuberpmgasto() {
		if(this.suberpmgasto==null)this.suberpmgasto=new ArrayList<>(0);
		  return this.suberpmgasto; 
	}
	
	public List<Erpmproveedor> getSuberpmproveedor() {
		if(this.suberpmproveedor==null)this.suberpmproveedor=new ArrayList<>(0);
		  return this.suberpmproveedor; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	}
	
	public List<Erpfacturae> getSuberpfacturae() {
		if(this.suberpfacturae==null)this.suberpfacturae=new ArrayList<>(0);
		  return this.suberpfacturae; 
	} 

}