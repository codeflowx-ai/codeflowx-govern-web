package org.suinsit.apps.facturacin;

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
import org.enartframework.nocode.annotacion.SequenceYear;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpformapago;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmestado;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.facturacin.Erprlineapedido;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPPEDIDO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPPEDIDO",
	labelMonitor = "PEDIDO",
	pk = "idxerppedido" 
)
public class Erppedido implements Serializable { 

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
	@Column (
		name = "facturado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
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
		label = "",
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
		label = "",
		type = "DATE" 
	)
	private Date fechaentrega;
	@Id
	@Column (
		name = "idxerppedido",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerppedido;
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
		label = "",
		type = "BOOLEAN" 
	)
	private boolean pagolinea;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pedido",
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
		createManual = false,
		createFromFieldValue = "",
		fieldPrefix = "",
		name = "ERPPEDIDO_PEDIDO",
		prefix = "",
		mask = "000000",
		addYear = true 
	)
	private String pedido;
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
		name = "IDERPEMPRESA0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa iderpempresa;
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
		name = "IDCRMOPORTUNIDAD0",
		referencedColumnName = "IDXOPORTUNIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmoportunidad idcrmoportunidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderppedido" 
	)
	private List<Erprlineapedido> suberprlineapedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderppedido" 
	)
	private List<Erppresupuesto> suberppresupuesto; 

	public Erpempresa getIderpempresa() {
		if(this.iderpempresa==null)this.iderpempresa=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.iderpempresa; 
	}
	
	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	}
	
	public Erpmestado getIderpmestado() {
		if(this.iderpmestado==null)this.iderpmestado=new org.suinsit.apps.facturacin.Erpmestado();
		  return this.iderpmestado; 
	}
	
	public Erppresupuesto getIderppresupuesto() {
		if(this.iderppresupuesto==null)this.iderppresupuesto=new org.suinsit.apps.facturacin.Erppresupuesto();
		  return this.iderppresupuesto; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public List<Erprlineapedido> getSuberprlineapedido() {
		if(this.suberprlineapedido==null)this.suberprlineapedido=new ArrayList<>(0);
		  return this.suberprlineapedido; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	} 

}