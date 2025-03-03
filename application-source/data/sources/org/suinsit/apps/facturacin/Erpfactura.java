package org.suinsit.apps.facturacin;

import java.io.Serializable;
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
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.SequenceYear;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Ctbcbrofact;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpfacturae;
import org.suinsit.apps.facturacin.Erpfactvenci;
import org.suinsit.apps.facturacin.Erpformapago;
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erplineafactura;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmestado;
import org.suinsit.apps.facturacin.Erpmestadocbo;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.facturacin.Erpseries;
import org.suinsit.apps.franchise.Frcmfranchise;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPFACTURA" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPFACTURA",
	labelMonitor = "Factura",
	pk = "idxerpfactura" 
)
public class Erpfactura implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 3500 
	)
	@Column (
		name = "aditionalinformation",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String aditionalinformation;
	@Column (
		name = "condiciones",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String condiciones;
	@Column (
		name = "contabilizado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean contabilizado;
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
		name = "enddate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date enddate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "factura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "AUTONUMERIC" 
	)
	@SequenceYear (
		autoCreateFromYear = true,
		autoResetYear = true,
		createManual = true,
		createFromFieldValue = "",
		fieldPrefix = "",
		foreing = true,
		fkTable = "ERPSERIES",
		fkAlias = "iderpseries",
		name = "ERPFACTURA_FACTURA",
		prefix = "",
		mask = "0000000000",
		addYear = true 
	)
	private String factura;
	@NotNull
	@NotBlank
	@Column (
		name = "fechafact",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechafact;
	@Column (
		name = "fecobro",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecobro;
	@Column (
		name = "fecsigned",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecsigned;
	@Id
	@Column (
		name = "idxerpfactura",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpfactura;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importrecargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importrecargo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importreten",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importreten;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impuestos",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Impuestos",
		type = "DECIMAL" 
	)
	private BigDecimal impuestos;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "infoadicional",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String infoadicional;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "numexpediente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String numexpediente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "oficinactble",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String oficinactble;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "organogtor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String organogtor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "organoprop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String organoprop;
	@Column (
		name = "pagolinea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean pagolinea;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentrecargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentrecargo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentreten",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentreten;
	@Size (
		min = 0,
		max = 300 
	)
	@Column (
		name = "rectificafactura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String rectificafactura;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refcliente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refcliente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refpedido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refpedido;
	@Column (
		name = "startdate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date startdate;
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
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "total",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal total;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "unidadtrmite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String unidadtrmite;
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
	@Column (
		name = "vencimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date vencimiento;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPPRESUPUESTO0",
		referencedColumnName = "IDXERPPRESUPUESTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erppresupuesto iderppresupuesto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMABNCO0",
		referencedColumnName = "IDXERPMABNCO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmabnco iderpmabnco;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMESTADO0",
		referencedColumnName = "IDXERPMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmestado iderpmestado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFORMAPAGO0",
		referencedColumnName = "IDXERPFORMAPAGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpformapago iderpformapago;
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
		name = "IDCOMPANY0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa idcompany;
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
		name = "IDERPMESTADOCBO0",
		referencedColumnName = "IDXERPMESTADOCBO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmestadocbo iderpmestadocbo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPSERIES0",
		referencedColumnName = "IDXERPSERIES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpseries iderpseries;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFACTURAE0",
		referencedColumnName = "IDXERPFACTURAE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfacturae iderpfacturae;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFACTURARECTI0",
		referencedColumnName = "IDXERPFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfactura idfacturarecti;
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
		mappedBy = "iderpfactura" 
	)
	private List<Ctbcbrofact> subctbcbrofact;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpfactura" 
	)
	private List<Erpfactvenci> suberpfactvenci;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpfactura" 
	)
	private List<Erplineafactura> suberplineafactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpfactura" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfacturarecti" 
	)
	private List<Erpfactura> suberpfactura; 

	public Erppresupuesto getIderppresupuesto() {
		if(this.iderppresupuesto==null)this.iderppresupuesto=new org.suinsit.apps.facturacin.Erppresupuesto();
		  return this.iderppresupuesto; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	}
	
	public Erpmestado getIderpmestado() {
		if(this.iderpmestado==null)this.iderpmestado=new org.suinsit.apps.facturacin.Erpmestado();
		  return this.iderpmestado; 
	}
	
	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpimpuestos getIderpimpuestos() {
		if(this.iderpimpuestos==null)this.iderpimpuestos=new org.suinsit.apps.facturacin.Erpimpuestos();
		  return this.iderpimpuestos; 
	}
	
	public Erpmestadocbo getIderpmestadocbo() {
		if(this.iderpmestadocbo==null)this.iderpmestadocbo=new org.suinsit.apps.facturacin.Erpmestadocbo();
		  return this.iderpmestadocbo; 
	}
	
	public Erpseries getIderpseries() {
		if(this.iderpseries==null)this.iderpseries=new org.suinsit.apps.facturacin.Erpseries();
		  return this.iderpseries; 
	}
	
	public Erpfacturae getIderpfacturae() {
		if(this.iderpfacturae==null)this.iderpfacturae=new org.suinsit.apps.facturacin.Erpfacturae();
		  return this.iderpfacturae; 
	}
	
	public Erpfactura getIdfacturarecti() {
		if(this.idfacturarecti==null)this.idfacturarecti=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.idfacturarecti; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Ctbcbrofact> getSubctbcbrofact() {
		if(this.subctbcbrofact==null)this.subctbcbrofact=new ArrayList<>(0);
		  return this.subctbcbrofact; 
	}
	
	public List<Erpfactvenci> getSuberpfactvenci() {
		if(this.suberpfactvenci==null)this.suberpfactvenci=new ArrayList<>(0);
		  return this.suberpfactvenci; 
	}
	
	public List<Erplineafactura> getSuberplineafactura() {
		if(this.suberplineafactura==null)this.suberplineafactura=new ArrayList<>(0);
		  return this.suberplineafactura; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	} 

}