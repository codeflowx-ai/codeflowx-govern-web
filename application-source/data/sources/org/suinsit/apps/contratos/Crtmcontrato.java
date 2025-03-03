package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Mperidicidad;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.contratos.Crtlineacontr;
import org.suinsit.apps.contratos.Crtmtipo;
import org.suinsit.apps.contratos.Crtrfactcontra;
import org.suinsit.apps.contratos.Crtrfirmascto;
import org.suinsit.apps.contratos.Crtrrenovacion;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.portalemp.Rrhhimputaciones;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRTMCONTRATO" 
)
@Entidad (
	namespace = "contratos",
	type = "TABLE",
	name = "CRTMCONTRATO",
	labelMonitor = "CODCONTRATO",
	pk = "idxcrtmcontrato" 
)
public class Crtmcontrato implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "automatica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean automatica;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codcontrato",
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
		name = "CRTMCONTRATO_CODCONTRATO",
		prefix = "CRT",
		mask = "00000",
		addYear = true 
	)
	private String codcontrato;
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
	@NotNull
	@NotBlank
	@Column (
		name = "fecefecto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecefecto;
	@Column (
		name = "fecfin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecfin;
	@Column (
		name = "firmarcliente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean firmarcliente;
	@Id
	@Column (
		name = "idxcrtmcontrato",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrtmcontrato;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
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
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCUSTOMER0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcustomer;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRTMTIPO0",
		referencedColumnName = "IDXCRTMTIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crtmtipo idcrtmtipo;
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
		name = "IDMPERIDICIDAD0",
		referencedColumnName = "IDXMPERIDICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mperidicidad idmperidicidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Crtrfactcontra> subcrtrfactcontra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Crtlineacontr> subcrtlineacontr;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Crtrfirmascto> subcrtrfirmascto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Docfichero> subdocfichero;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Crtrrenovacion> subcrtrrenovacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrtmcontrato" 
	)
	private List<Rrhhimputaciones> subrrhhimputaciones; 

	public Crmempresa getIdcustomer() {
		if(this.idcustomer==null)this.idcustomer=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcustomer; 
	}
	
	public Crtmtipo getIdcrtmtipo() {
		if(this.idcrtmtipo==null)this.idcrtmtipo=new org.suinsit.apps.contratos.Crtmtipo();
		  return this.idcrtmtipo; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Erpempresa getIderpempresa() {
		if(this.iderpempresa==null)this.iderpempresa=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.iderpempresa; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Mperidicidad getIdmperidicidad() {
		if(this.idmperidicidad==null)this.idmperidicidad=new org.suinsit.apps.admin.Mperidicidad();
		  return this.idmperidicidad; 
	}
	
	public List<Crtrfactcontra> getSubcrtrfactcontra() {
		if(this.subcrtrfactcontra==null)this.subcrtrfactcontra=new ArrayList<>(0);
		  return this.subcrtrfactcontra; 
	}
	
	public List<Crtlineacontr> getSubcrtlineacontr() {
		if(this.subcrtlineacontr==null)this.subcrtlineacontr=new ArrayList<>(0);
		  return this.subcrtlineacontr; 
	}
	
	public List<Crtrfirmascto> getSubcrtrfirmascto() {
		if(this.subcrtrfirmascto==null)this.subcrtrfirmascto=new ArrayList<>(0);
		  return this.subcrtrfirmascto; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	}
	
	public List<Crtrrenovacion> getSubcrtrrenovacion() {
		if(this.subcrtrrenovacion==null)this.subcrtrrenovacion=new ArrayList<>(0);
		  return this.subcrtrrenovacion; 
	}
	
	public List<Rrhhimputaciones> getSubrrhhimputaciones() {
		if(this.subrrhhimputaciones==null)this.subrrhhimputaciones=new ArrayList<>(0);
		  return this.subrrhhimputaciones; 
	} 

}