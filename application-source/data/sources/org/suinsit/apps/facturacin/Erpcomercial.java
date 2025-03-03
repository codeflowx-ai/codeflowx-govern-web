package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.contratos.Crtmcontrato;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.facturacin.Erpequipo;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.partners.Partner;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPCOMERCIAL" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPCOMERCIAL",
	labelMonitor = "nombre",
	pk = "idxerpcomercial" 
)
public class Erpcomercial implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "franquiciado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean franquiciado;
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
		name = "email",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String email;
	@Id
	@Column (
		name = "idxerpcomercial",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpcomercial;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "movil",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String movil;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "objetivo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Objetivo mensual",
		type = "DECIMAL" 
	)
	private BigDecimal objetivo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String telefono;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPEQUIPO0",
		referencedColumnName = "IDXERPEQUIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpequipo iderpequipo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFRCMFRANCHISE0",
		referencedColumnName = "IDXFRCMFRANCHISE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frcmfranchise idfrcmfranchise;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idlider" 
	)
	private List<Erpequipo> suberpequipo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Crtmcontrato> subcrtmcontrato;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Partner> subpartner;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Subscripcion> subsubscripcion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Erppedido> suberppedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Almproject> subalmproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Frcmfranchise> subfrcmfranchise;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpcomercial" 
	)
	private List<Crmoportunidad> subcrmoportunidad; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Erpequipo getIderpequipo() {
		if(this.iderpequipo==null)this.iderpequipo=new org.suinsit.apps.facturacin.Erpequipo();
		  return this.iderpequipo; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Erpequipo> getSuberpequipo() {
		if(this.suberpequipo==null)this.suberpequipo=new ArrayList<>(0);
		  return this.suberpequipo; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Crtmcontrato> getSubcrtmcontrato() {
		if(this.subcrtmcontrato==null)this.subcrtmcontrato=new ArrayList<>(0);
		  return this.subcrtmcontrato; 
	}
	
	public List<Partner> getSubpartner() {
		if(this.subpartner==null)this.subpartner=new ArrayList<>(0);
		  return this.subpartner; 
	}
	
	public List<Subscripcion> getSubsubscripcion() {
		if(this.subsubscripcion==null)this.subsubscripcion=new ArrayList<>(0);
		  return this.subsubscripcion; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	}
	
	public List<Frcmfranchise> getSubfrcmfranchise() {
		if(this.subfrcmfranchise==null)this.subfrcmfranchise=new ArrayList<>(0);
		  return this.subfrcmfranchise; 
	}
	
	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	} 

}