package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
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
import org.enartframework.nocode.annotacion.FieldType;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.portalemp.Rhfichar;
import org.suinsit.apps.portalemp.Rrhhausencia;
import org.suinsit.apps.portalemp.Rrhhcargolab;
import org.suinsit.apps.portalemp.Rrhhestadoemp;
import org.suinsit.apps.portalemp.Rrhhmacademic;
import org.suinsit.apps.portalemp.Rrhhmcatlab;
import org.suinsit.apps.portalemp.Rrhhmotalta;
import org.suinsit.apps.portalemp.Rrhhmotivobaja;
import org.suinsit.apps.portalemp.Rrhhrempcontra;
import org.suinsit.apps.portalemp.Rrhhrempgrupo;
import org.suinsit.apps.portalemp.Rrhmtipoconta;
import org.suinsit.apps.portalemp.Rrhrempdoc;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHEMPLEADO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHEMPLEADO",
	labelMonitor = "NOMBRE",
	pk = "idxrrhempleado" 
)
public class Rrhempleado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "numempleado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer numempleado;
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
	@Column (
		name = "altaempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date altaempresa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "antiguedad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String antiguedad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "apellidos",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Apellidos",
		type = "VARCHAR" 
	)
	private String apellidos;
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
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@Column (
		name = "bajaempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date bajaempresa;
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
		name = "ciudad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ciudad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codigo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codpostal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codpostal;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "correo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Email",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String correo;
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
		label = "Categoría",
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
		label = "Categoría",
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "departamento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String departamento;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "dependientes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal dependientes;
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
	@Column (
		name = "directivo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean directivo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "discapacidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal discapacidad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailpersonal",
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
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String emailpersonal;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Soltero/a,Casado/a,Pareja de hecho" 
		},
		message = "solamente admite lo valores: ,Soltero/a,Casado/a,Pareja de hecho" 
	)
	@Column (
		name = "estadocivil",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String estadocivil;
	@Column (
		name = "fecprevistaalta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecprevistaalta;
	@Column (
		name = "fecprevistabaja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecprevistabaja;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "hijos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal hijos;
	@Column (
		name = "horasprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer horasprev;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "identificador",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "DNI / NIF / NIE",
		type = "VARCHAR" 
	)
	private String identificador;
	@Id
	@Column (
		name = "idxrrhempleado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhempleado;
	@Column (
		name = "lastaccessportal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp lastaccessportal;
	@Column (
		name = "lockportal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Bloquear Acceso portal",
		type = "BOOLEAN" 
	)
	private boolean lockportal;
	@Column (
		name = "motivoalta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String motivoalta;
	@Column (
		name = "motivobaja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String motivobaja;
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
	@Column (
		name = "nacimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fec. Nacimiento",
		type = "DATE" 
	)
	private Date nacimiento;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Nombre",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Residente,No Residente" 
		},
		message = "solamente admite lo valores: ,Residente,No Residente" 
	)
	@Column (
		name = "residenfiscal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String residenfiscal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "retenciones",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal retenciones;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "salarioanual",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal salarioanual;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "salariodia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal salariodia;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "salariomes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal salariomes;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "seguridadsocial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Seguridad Social",
		type = "VARCHAR" 
	)
	private String seguridadsocial;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Hombre,Mujer" 
		},
		message = "solamente admite lo valores: ,Hombre,Mujer" 
	)
	@Column (
		name = "sexo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String sexo;
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
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "Fec. Nacimiento",
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
		label = "Fec. Nacimiento",
		type = "VARCHAR" 
	)
	private String tmmodif;
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
		name = "IDRRHHMACADEMIC0",
		referencedColumnName = "IDXRRHHMACADEMIC",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhmacademic idrrhhmacademic;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHMOTIVOBAJA0",
		referencedColumnName = "IDXRRHHMOTIVOBAJA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhmotivobaja idrrhhmotivobaja;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHMOTALTA0",
		referencedColumnName = "IDXRRHHMOTALTA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhmotalta idrrhhmotalta;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHCARGOLAB0",
		referencedColumnName = "IDXRRHHCARGOLAB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhcargolab idrrhhcargolab;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHMCATLAB0",
		referencedColumnName = "IDXRRHHMCATLAB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhmcatlab idrrhhmcatlab;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHESTADOEMP0",
		referencedColumnName = "IDXRRHHESTADOEMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhestadoemp idrrhhestadoemp;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNACIONALIDAD0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idnacionalidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDIDIOMA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais ididioma;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHMTIPOCONTA0",
		referencedColumnName = "IDXRRHMTIPOCONTA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhmtipoconta idrrhmtipoconta;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Rrhhrempcontra> subrrhhrempcontra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Rrhhrempgrupo> subrrhhrempgrupo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Rrhhausencia> subrrhhausencia;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Rhfichar> subrhfichar;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Rrhrempdoc> subrrhrempdoc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhempleado" 
	)
	private List<Docfichero> subdocfichero; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public Rrhhmacademic getIdrrhhmacademic() {
		if(this.idrrhhmacademic==null)this.idrrhhmacademic=new org.suinsit.apps.portalemp.Rrhhmacademic();
		  return this.idrrhhmacademic; 
	}
	
	public Rrhhmotivobaja getIdrrhhmotivobaja() {
		if(this.idrrhhmotivobaja==null)this.idrrhhmotivobaja=new org.suinsit.apps.portalemp.Rrhhmotivobaja();
		  return this.idrrhhmotivobaja; 
	}
	
	public Rrhhmotalta getIdrrhhmotalta() {
		if(this.idrrhhmotalta==null)this.idrrhhmotalta=new org.suinsit.apps.portalemp.Rrhhmotalta();
		  return this.idrrhhmotalta; 
	}
	
	public Rrhhcargolab getIdrrhhcargolab() {
		if(this.idrrhhcargolab==null)this.idrrhhcargolab=new org.suinsit.apps.portalemp.Rrhhcargolab();
		  return this.idrrhhcargolab; 
	}
	
	public Rrhhmcatlab getIdrrhhmcatlab() {
		if(this.idrrhhmcatlab==null)this.idrrhhmcatlab=new org.suinsit.apps.portalemp.Rrhhmcatlab();
		  return this.idrrhhmcatlab; 
	}
	
	public Rrhhestadoemp getIdrrhhestadoemp() {
		if(this.idrrhhestadoemp==null)this.idrrhhestadoemp=new org.suinsit.apps.portalemp.Rrhhestadoemp();
		  return this.idrrhhestadoemp; 
	}
	
	public Mpais getIdnacionalidad() {
		if(this.idnacionalidad==null)this.idnacionalidad=new org.suinsit.apps.admin.Mpais();
		  return this.idnacionalidad; 
	}
	
	public Mpais getIdidioma() {
		if(this.ididioma==null)this.ididioma=new org.suinsit.apps.admin.Mpais();
		  return this.ididioma; 
	}
	
	public Rrhmtipoconta getIdrrhmtipoconta() {
		if(this.idrrhmtipoconta==null)this.idrrhmtipoconta=new org.suinsit.apps.portalemp.Rrhmtipoconta();
		  return this.idrrhmtipoconta; 
	}
	
	public List<Rrhhrempcontra> getSubrrhhrempcontra() {
		if(this.subrrhhrempcontra==null)this.subrrhhrempcontra=new ArrayList<>(0);
		  return this.subrrhhrempcontra; 
	}
	
	public List<Rrhhrempgrupo> getSubrrhhrempgrupo() {
		if(this.subrrhhrempgrupo==null)this.subrrhhrempgrupo=new ArrayList<>(0);
		  return this.subrrhhrempgrupo; 
	}
	
	public List<Rrhhausencia> getSubrrhhausencia() {
		if(this.subrrhhausencia==null)this.subrrhhausencia=new ArrayList<>(0);
		  return this.subrrhhausencia; 
	}
	
	public List<Rhfichar> getSubrhfichar() {
		if(this.subrhfichar==null)this.subrhfichar=new ArrayList<>(0);
		  return this.subrhfichar; 
	}
	
	public List<Rrhrempdoc> getSubrrhrempdoc() {
		if(this.subrrhrempdoc==null)this.subrrhrempdoc=new ArrayList<>(0);
		  return this.subrrhrempdoc; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	} 

}