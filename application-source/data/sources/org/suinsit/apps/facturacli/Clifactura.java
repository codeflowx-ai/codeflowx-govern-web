package org.suinsit.apps.facturacli;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.SequenceYear;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpformapago;
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erpmestado;
import org.suinsit.apps.facturacin.Erpmestadocbo;
import org.suinsit.apps.facturacli.Clicliente;
import org.suinsit.apps.facturacli.Clilineafactura;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CLIFACTURA" 
)
@Entidad (
	namespace = "facturacli",
	type = "TABLE",
	name = "CLIFACTURA",
	labelMonitor = "FACTURA",
	pk = "idxclifactura" 
)
public class Clifactura implements Serializable { 

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
		autoResetYear = false,
		createManual = false,
		createFromFieldValue = "idcrmempresa.idxcrmempresa",
		fieldPrefix = "serie",
		name = "CLIFACTURA_FACTURA",
		prefix = "",
		mask = "000000",
		addYear = false 
	)
	private String factura;
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
	@NotNull
	@NotBlank
	@Column (
		name = "fechafact",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "Fecha Factura",
		type = "DATE" 
	)
	private Date fechafact;
	@Id
	@Column (
		name = "idxclifactura",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxclifactura;
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
		auditar = true,
		filter = true,
		label = "",
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
		auditar = true,
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",AA,BB,CC" 
		},
		message = "solamente admite lo valores: ,AA,BB,CC" 
	)
	@Column (
		name = "serie",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String serie;
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
		auditar = true,
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
		auditar = true,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal total;
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
	@NotNull
	@NotBlank
	@Column (
		name = "vencimiento",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "Vencimiento",
		type = "DATE" 
	)
	private Date vencimiento;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
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
		name = "IDCLICLIENTE0",
		referencedColumnName = "IDXCLICLIENTE",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Clicliente idclicliente;
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
		name = "IDERPMESTADO0",
		referencedColumnName = "IDXERPMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmestado iderpmestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idclifactura" 
	)
	private List<Clilineafactura> subclilineafactura; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Erpmestadocbo getIderpmestadocbo() {
		if(this.iderpmestadocbo==null)this.iderpmestadocbo=new org.suinsit.apps.facturacin.Erpmestadocbo();
		  return this.iderpmestadocbo; 
	}
	
	public Clicliente getIdclicliente() {
		if(this.idclicliente==null)this.idclicliente=new org.suinsit.apps.facturacli.Clicliente();
		  return this.idclicliente; 
	}
	
	public Erpimpuestos getIderpimpuestos() {
		if(this.iderpimpuestos==null)this.iderpimpuestos=new org.suinsit.apps.facturacin.Erpimpuestos();
		  return this.iderpimpuestos; 
	}
	
	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	}
	
	public Erpmestado getIderpmestado() {
		if(this.iderpmestado==null)this.iderpmestado=new org.suinsit.apps.facturacin.Erpmestado();
		  return this.iderpmestado; 
	}
	
	public List<Clilineafactura> getSubclilineafactura() {
		if(this.subclilineafactura==null)this.subclilineafactura=new ArrayList<>(0);
		  return this.subclilineafactura; 
	} 

}