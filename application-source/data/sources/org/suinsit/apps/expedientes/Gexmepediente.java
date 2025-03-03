package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.expedientes.Gexexprfact;
import org.suinsit.apps.expedientes.Gexmactuacion;
import org.suinsit.apps.expedientes.Gexmestado;
import org.suinsit.apps.expedientes.Gexmtipo;
import org.suinsit.apps.expedientes.Gexrfiirmas;
import org.suinsit.apps.expedientes.Gexrnota;
import org.suinsit.apps.expedientes.Gexrreqexp;
import org.suinsit.apps.expedientes.Gexrtiporesol;
import org.suinsit.apps.portalemp.Rrhhimputaciones;
import org.suinsit.apps.subvenciones.Submorganismo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXMEPEDIENTE" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMEPEDIENTE",
	labelMonitor = "expediente",
	pk = "idxgexmepediente" 
)
public class Gexmepediente implements Serializable { 

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
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
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
		name = "descreolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descreolucion;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "docpendiente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String docpendiente;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "expediente",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "GEXMEPEDIENTE_EXPEDIENTE",
		prefix = "GEX",
		mask = "000000",
		addYear = true 
	)
	private String expediente;
	@Column (
		name = "fecresolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecresolucion;
	@Column (
		name = "fectramite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fectramite;
	@Column (
		name = "fecvto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecvto;
	@Id
	@Column (
		name = "idxgexmepediente",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmepediente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refadministrativa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refadministrativa;
	@Column (
		name = "requerimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object requerimiento;
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
		name = "IDGEXMTIPO0",
		referencedColumnName = "IDXGEXMTIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmtipo idgexmtipo;
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
		name = "IDSUBMORGANISMO0",
		referencedColumnName = "IDXSUBMORGANISMO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Submorganismo idsubmorganismo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMESTADO0",
		referencedColumnName = "IDXGEXMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmestado idgexmestado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXRTIPORESOL0",
		referencedColumnName = "IDXGEXRTIPORESOL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexrtiporesol idgexrtiporesol;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Gexmactuacion> subgexmactuacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Gexrnota> subgexrnota;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Gexexprfact> subgexexprfact;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Docfichero> subdocfichero;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Gexrfiirmas> subgexrfiirmas;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Rrhhimputaciones> subrrhhimputaciones;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmepediente" 
	)
	private List<Gexrreqexp> subgexrreqexp; 

	public Gexmtipo getIdgexmtipo() {
		if(this.idgexmtipo==null)this.idgexmtipo=new org.suinsit.apps.expedientes.Gexmtipo();
		  return this.idgexmtipo; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Submorganismo getIdsubmorganismo() {
		if(this.idsubmorganismo==null)this.idsubmorganismo=new org.suinsit.apps.subvenciones.Submorganismo();
		  return this.idsubmorganismo; 
	}
	
	public Gexmestado getIdgexmestado() {
		if(this.idgexmestado==null)this.idgexmestado=new org.suinsit.apps.expedientes.Gexmestado();
		  return this.idgexmestado; 
	}
	
	public Gexrtiporesol getIdgexrtiporesol() {
		if(this.idgexrtiporesol==null)this.idgexrtiporesol=new org.suinsit.apps.expedientes.Gexrtiporesol();
		  return this.idgexrtiporesol; 
	}
	
	public List<Gexmactuacion> getSubgexmactuacion() {
		if(this.subgexmactuacion==null)this.subgexmactuacion=new ArrayList<>(0);
		  return this.subgexmactuacion; 
	}
	
	public List<Gexrnota> getSubgexrnota() {
		if(this.subgexrnota==null)this.subgexrnota=new ArrayList<>(0);
		  return this.subgexrnota; 
	}
	
	public List<Gexexprfact> getSubgexexprfact() {
		if(this.subgexexprfact==null)this.subgexexprfact=new ArrayList<>(0);
		  return this.subgexexprfact; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	}
	
	public List<Gexrfiirmas> getSubgexrfiirmas() {
		if(this.subgexrfiirmas==null)this.subgexrfiirmas=new ArrayList<>(0);
		  return this.subgexrfiirmas; 
	}
	
	public List<Rrhhimputaciones> getSubrrhhimputaciones() {
		if(this.subrrhhimputaciones==null)this.subrrhhimputaciones=new ArrayList<>(0);
		  return this.subrrhhimputaciones; 
	}
	
	public List<Gexrreqexp> getSubgexrreqexp() {
		if(this.subgexrreqexp==null)this.subgexrreqexp=new ArrayList<>(0);
		  return this.subgexrreqexp; 
	} 

}