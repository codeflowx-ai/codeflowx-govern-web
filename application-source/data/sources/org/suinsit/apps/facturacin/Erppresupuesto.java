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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.SequenceYear;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpformapago;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmestado;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erprlineapedido;
import org.suinsit.apps.franchise.Frcmfranchise;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPPRESUPUESTO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPPRESUPUESTO",
	labelMonitor = "Presupuesto",
	pk = "idxerppresupuesto" 
)
public class Erppresupuesto implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "facturado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Facturado",
		type = "BOOLEAN" 
	)
	private boolean facturado;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha",
		type = "DATE" 
	)
	private Date fecha;
	@Column (
		name = "fechaentrega",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha Entrega",
		type = "DATE" 
	)
	private Date fechaentrega;
	@Id
	@Column (
		name = "idxerppresupuesto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerppresupuesto;
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
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impuestos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impuestos;
	@Column (
		name = "pagolinea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Pago en linea",
		type = "BOOLEAN" 
	)
	private boolean pagolinea;
	@Column (
		name = "pedido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean pedido;
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
		max = 100 
	)
	@Column (
		name = "presupuesto",
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
		autoCreateFromYear = false,
		autoResetYear = false,
		createManual = true,
		createFromFieldValue = "",
		fieldPrefix = "",
		name = "ERPPRESUPUESTO_PRESUPUESTO",
		prefix = "",
		mask = "0000000000",
		addYear = true 
	)
	private String presupuesto;
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
		label = "Referencia Cliente",
		type = "VARCHAR" 
	)
	private String refcliente;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLIENTE0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcliente;
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
		name = "IDCRMOPORTUNIDAD0",
		referencedColumnName = "IDXOPORTUNIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmoportunidad idcrmoportunidad;
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
		name = "IDERPPEDIDO0",
		referencedColumnName = "IDXERPPEDIDO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erppedido iderppedido;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFACTURA0",
		referencedColumnName = "IDXERPFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfactura iderpfactura;
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
		mappedBy = "iderppresupuesto" 
	)
	private List<Erprlineapedido> suberprlineapedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderppresupuesto" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderppresupuesto" 
	)
	private List<Erppedido> suberppedido; 

	public Crmempresa getIdcliente() {
		if(this.idcliente==null)this.idcliente=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcliente; 
	}
	
	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	}
	
	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Erpmestado getIderpmestado() {
		if(this.iderpmestado==null)this.iderpmestado=new org.suinsit.apps.facturacin.Erpmestado();
		  return this.iderpmestado; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Erppedido getIderppedido() {
		if(this.iderppedido==null)this.iderppedido=new org.suinsit.apps.facturacin.Erppedido();
		  return this.iderppedido; 
	}
	
	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Erprlineapedido> getSuberprlineapedido() {
		if(this.suberprlineapedido==null)this.suberprlineapedido=new ArrayList<>(0);
		  return this.suberprlineapedido; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	} 

}