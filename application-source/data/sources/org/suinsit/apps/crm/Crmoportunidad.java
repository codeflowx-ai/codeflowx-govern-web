package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Mtipooportunidad;
import org.suinsit.apps.admin.Mtipoprioridad;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.asesor4.Sacticket;
import org.suinsit.apps.citaprev.Citmagenda;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmetapa;
import org.suinsit.apps.crm.Crmhistetapas;
import org.suinsit.apps.crm.Crmllamada;
import org.suinsit.apps.crm.Crmnota;
import org.suinsit.apps.crm.Crmoportllamada;
import org.suinsit.apps.crm.Crmpipeline;
import org.suinsit.apps.crm.Crmropocontact;
import org.suinsit.apps.crm.Crmropodocs;
import org.suinsit.apps.crm.Crmroporcomp;
import org.suinsit.apps.crm.Crmroporemp;
import org.suinsit.apps.crm.Crmroporprodu;
import org.suinsit.apps.crm.Crmroportagenda;
import org.suinsit.apps.crm.Crmroporticket;
import org.suinsit.apps.crm.Crmroportnotas;
import org.suinsit.apps.crm.Crmropotask;
import org.suinsit.apps.crm.Crmtarea;
import org.suinsit.apps.crm.Crmtiponegocio;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.marketing.Mktrpotoportunidad;
import org.suinsit.apps.partners.Partner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMOPORTUNIDAD" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMOPORTUNIDAD",
	labelMonitor = "OPORTUNIDAD",
	pk = "idxoportunidad" 
)
public class Crmoportunidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "archived",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean archived;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@Column (
		name = "cerrado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cerrado;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigouuid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "uuid",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String codigouuid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String couseralta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Descripción",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "feccierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccierre;
	@Column (
		name = "feccierreestima",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccierreestima;
	@Column (
		name = "fectstart",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha prevista",
		type = "DATE" 
	)
	private Date fectstart;
	@Id
	@Column (
		name = "idxoportunidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxoportunidad;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importefinal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importefinal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importeprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "Importe Previsto",
		type = "DECIMAL" 
	)
	private BigDecimal importeprev;
	@Column (
		name = "motivocierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String motivocierre;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "oportunidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "oportunidad",
		type = "VARCHAR" 
	)
	private String oportunidad;
	@Column (
		name = "position",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer position;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "probabilidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal probabilidad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String status;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmmodif;
	@Column (
		name = "ultimocontacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date ultimocontacto;
	@Column (
		name = "ultmodificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp ultmodificacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usariomodif",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usariomodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMTIPOOPORTUNIDAD0",
		referencedColumnName = "IDXTIPOOPORTUNIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mtipooportunidad idmtipooportunidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMTIPOPRIORIDAD0",
		referencedColumnName = "IDXTIPOPRIORIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mtipoprioridad idmtipoprioridad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROPIETARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idpropietario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMETAPA0",
		referencedColumnName = "IDXCRMETAPA",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmetapa idcrmetapa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMPIPELINE0",
		referencedColumnName = "IDXCRMPIPELINE",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmpipeline idcrmpipeline;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMTIPONEGOCIO0",
		referencedColumnName = "IDXCRMTIPONEGOCIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtiponegocio idcrmtiponegocio;
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
		name = "IDFRCMFRANCHISE0",
		referencedColumnName = "IDXFRCMFRANCHISE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frcmfranchise idfrcmfranchise;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPARTNER0",
		referencedColumnName = "IDXPARTNER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Partner idpartner;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroporemp> subcrmroporemp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmropocontact> subcrmropocontact;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroporprodu> subcrmroporprodu;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmropodocs> subcrmropodocs;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroportnotas> subcrmroportnotas;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmropotask> subcrmropotask;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroportagenda> subcrmroportagenda;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmoportllamada> subcrmoportllamada;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroporcomp> subcrmroporcomp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmroporticket> subcrmroporticket;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmnota> subcrmnota;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmllamada> subcrmllamada;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Sacticket> subsacticket;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmhistetapas> subcrmhistetapas;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Citmagenda> subcitmagenda;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Crmtarea> subcrmtarea;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Docfichero> subdocfichero;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Erppedido> suberppedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmoportunidad" 
	)
	private List<Mktrpotoportunidad> submktrpotoportunidad; 

	public Mtipooportunidad getIdmtipooportunidad() {
		if(this.idmtipooportunidad==null)this.idmtipooportunidad=new org.suinsit.apps.admin.Mtipooportunidad();
		  return this.idmtipooportunidad; 
	}
	
	public Mtipoprioridad getIdmtipoprioridad() {
		if(this.idmtipoprioridad==null)this.idmtipoprioridad=new org.suinsit.apps.admin.Mtipoprioridad();
		  return this.idmtipoprioridad; 
	}
	
	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Crmetapa getIdcrmetapa() {
		if(this.idcrmetapa==null)this.idcrmetapa=new org.suinsit.apps.crm.Crmetapa();
		  return this.idcrmetapa; 
	}
	
	public Crmpipeline getIdcrmpipeline() {
		if(this.idcrmpipeline==null)this.idcrmpipeline=new org.suinsit.apps.crm.Crmpipeline();
		  return this.idcrmpipeline; 
	}
	
	public Crmtiponegocio getIdcrmtiponegocio() {
		if(this.idcrmtiponegocio==null)this.idcrmtiponegocio=new org.suinsit.apps.crm.Crmtiponegocio();
		  return this.idcrmtiponegocio; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	}
	
	public List<Crmroporemp> getSubcrmroporemp() {
		if(this.subcrmroporemp==null)this.subcrmroporemp=new ArrayList<>(0);
		  return this.subcrmroporemp; 
	}
	
	public List<Crmropocontact> getSubcrmropocontact() {
		if(this.subcrmropocontact==null)this.subcrmropocontact=new ArrayList<>(0);
		  return this.subcrmropocontact; 
	}
	
	public List<Crmroporprodu> getSubcrmroporprodu() {
		if(this.subcrmroporprodu==null)this.subcrmroporprodu=new ArrayList<>(0);
		  return this.subcrmroporprodu; 
	}
	
	public List<Crmropodocs> getSubcrmropodocs() {
		if(this.subcrmropodocs==null)this.subcrmropodocs=new ArrayList<>(0);
		  return this.subcrmropodocs; 
	}
	
	public List<Crmroportnotas> getSubcrmroportnotas() {
		if(this.subcrmroportnotas==null)this.subcrmroportnotas=new ArrayList<>(0);
		  return this.subcrmroportnotas; 
	}
	
	public List<Crmropotask> getSubcrmropotask() {
		if(this.subcrmropotask==null)this.subcrmropotask=new ArrayList<>(0);
		  return this.subcrmropotask; 
	}
	
	public List<Crmroportagenda> getSubcrmroportagenda() {
		if(this.subcrmroportagenda==null)this.subcrmroportagenda=new ArrayList<>(0);
		  return this.subcrmroportagenda; 
	}
	
	public List<Crmoportllamada> getSubcrmoportllamada() {
		if(this.subcrmoportllamada==null)this.subcrmoportllamada=new ArrayList<>(0);
		  return this.subcrmoportllamada; 
	}
	
	public List<Crmroporcomp> getSubcrmroporcomp() {
		if(this.subcrmroporcomp==null)this.subcrmroporcomp=new ArrayList<>(0);
		  return this.subcrmroporcomp; 
	}
	
	public List<Crmroporticket> getSubcrmroporticket() {
		if(this.subcrmroporticket==null)this.subcrmroporticket=new ArrayList<>(0);
		  return this.subcrmroporticket; 
	}
	
	public List<Crmnota> getSubcrmnota() {
		if(this.subcrmnota==null)this.subcrmnota=new ArrayList<>(0);
		  return this.subcrmnota; 
	}
	
	public List<Crmllamada> getSubcrmllamada() {
		if(this.subcrmllamada==null)this.subcrmllamada=new ArrayList<>(0);
		  return this.subcrmllamada; 
	}
	
	public List<Sacticket> getSubsacticket() {
		if(this.subsacticket==null)this.subsacticket=new ArrayList<>(0);
		  return this.subsacticket; 
	}
	
	public List<Crmhistetapas> getSubcrmhistetapas() {
		if(this.subcrmhistetapas==null)this.subcrmhistetapas=new ArrayList<>(0);
		  return this.subcrmhistetapas; 
	}
	
	public List<Citmagenda> getSubcitmagenda() {
		if(this.subcitmagenda==null)this.subcitmagenda=new ArrayList<>(0);
		  return this.subcitmagenda; 
	}
	
	public List<Crmtarea> getSubcrmtarea() {
		if(this.subcrmtarea==null)this.subcrmtarea=new ArrayList<>(0);
		  return this.subcrmtarea; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	}
	
	public List<Mktrpotoportunidad> getSubmktrpotoportunidad() {
		if(this.submktrpotoportunidad==null)this.submktrpotoportunidad=new ArrayList<>(0);
		  return this.submktrpotoportunidad; 
	} 

}