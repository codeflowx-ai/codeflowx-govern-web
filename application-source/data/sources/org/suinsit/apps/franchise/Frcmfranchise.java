package org.suinsit.apps.franchise;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.atlas.Atldomain;
import org.suinsit.apps.atlas.Atlkubecloud;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.facturacin.Erpseries;
import org.suinsit.apps.franchise.Frcrobjetivos;
import org.suinsit.apps.franchise.Frcrprovfranc;
import org.suinsit.apps.franchise.Frctipo;
import org.suinsit.apps.marketing.Mktproject;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.partners.Partner;
import org.suinsit.apps.subvenciones.Kitdigital;
import org.suinsit.apps.subvenciones.Subsolictudes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FRCMFRANCHISE" 
)
@Entidad (
	namespace = "franchise",
	type = "TABLE",
	name = "FRCMFRANCHISE",
	labelMonitor = "codfranquiciado",
	pk = "idxfrcmfranchise" 
)
public class Frcmfranchise implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailcomercial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String emailcomercial;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailsupport",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String emailsupport;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailadmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String emailadmin;
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
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cannon",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cannon;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codfranquiciado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "FRCMFRANCHISE_CODFRANQUICIADO",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codfranquiciado;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "direccion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String direccion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "factestimada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal factestimada;
	@Column (
		name = "fincontrato",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fincontrato;
	@Id
	@Column (
		name = "idxfrcmfranchise",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfrcmfranchise;
	@Column (
		name = "iniciocontrato",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date iniciocontrato;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "instagram",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String instagram;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "invesdes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal invesdes;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "linkldn",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String linkldn;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "marketing",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal marketing;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "meta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String meta;
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
		name = "objetivoanual",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal objetivoanual;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "poblacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String poblacion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "royalti",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal royalti;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefonos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String telefonos;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "weburlweb",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String weburlweb;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPAIS0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idmpais;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPCOMERCIAL0",
		referencedColumnName = "IDXERPCOMERCIAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpcomercial iderpcomercial;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPROVINCIA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mprovincia idmprovincia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFRCTIPO0",
		referencedColumnName = "IDXFRCTIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frctipo idfrctipo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Subsolictudes> subsubsolictudes;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Almproject> subalmproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Frcrprovfranc> subfrcrprovfranc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Crmempresa> subcrmempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Partner> subpartner;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Erpseries> suberpseries;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Erppedido> suberppedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Kitdigital> subkitdigital;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Atlkubecloud> subatlkubecloud;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Atlproject> subatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Mktproject> submktproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Atldomain> subatldomain;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Frcrobjetivos> subfrcrobjetivos;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrcmfranchise" 
	)
	private List<Crmoportunidad> subcrmoportunidad; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public Frctipo getIdfrctipo() {
		if(this.idfrctipo==null)this.idfrctipo=new org.suinsit.apps.franchise.Frctipo();
		  return this.idfrctipo; 
	}
	
	public List<Subsolictudes> getSubsubsolictudes() {
		if(this.subsubsolictudes==null)this.subsubsolictudes=new ArrayList<>(0);
		  return this.subsubsolictudes; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	}
	
	public List<Frcrprovfranc> getSubfrcrprovfranc() {
		if(this.subfrcrprovfranc==null)this.subfrcrprovfranc=new ArrayList<>(0);
		  return this.subfrcrprovfranc; 
	}
	
	public List<Crmempresa> getSubcrmempresa() {
		if(this.subcrmempresa==null)this.subcrmempresa=new ArrayList<>(0);
		  return this.subcrmempresa; 
	}
	
	public List<Partner> getSubpartner() {
		if(this.subpartner==null)this.subpartner=new ArrayList<>(0);
		  return this.subpartner; 
	}
	
	public List<Erpseries> getSuberpseries() {
		if(this.suberpseries==null)this.suberpseries=new ArrayList<>(0);
		  return this.suberpseries; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Kitdigital> getSubkitdigital() {
		if(this.subkitdigital==null)this.subkitdigital=new ArrayList<>(0);
		  return this.subkitdigital; 
	}
	
	public List<Atlkubecloud> getSubatlkubecloud() {
		if(this.subatlkubecloud==null)this.subatlkubecloud=new ArrayList<>(0);
		  return this.subatlkubecloud; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	}
	
	public List<Mktproject> getSubmktproject() {
		if(this.submktproject==null)this.submktproject=new ArrayList<>(0);
		  return this.submktproject; 
	}
	
	public List<Atldomain> getSubatldomain() {
		if(this.subatldomain==null)this.subatldomain=new ArrayList<>(0);
		  return this.subatldomain; 
	}
	
	public List<Frcrobjetivos> getSubfrcrobjetivos() {
		if(this.subfrcrobjetivos==null)this.subfrcrobjetivos=new ArrayList<>(0);
		  return this.subfrcrobjetivos; 
	}
	
	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	} 

}