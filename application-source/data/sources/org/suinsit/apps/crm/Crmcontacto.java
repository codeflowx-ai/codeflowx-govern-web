package org.suinsit.apps.crm;

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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.FieldType;
import org.suinsit.apps.admin.Mcargo;
import org.suinsit.apps.admin.Morigen;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.asesor4.Mktcorreo;
import org.suinsit.apps.citaprev.Citmagenda;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmllamada;
import org.suinsit.apps.crm.Crmnota;
import org.suinsit.apps.crm.Crmrcontacempresa;
import org.suinsit.apps.crm.Crmtarea;
import org.suinsit.apps.facturacin.Erpmgasto;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.marketing.Mktrlistcustomer;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMCONTACTO" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMCONTACTO",
	labelMonitor = "NOMBRE",
	pk = "idxcrmcontacto" 
)
public class Crmcontacto implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		label = "apellidos",
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
		filter = false,
		label = "",
		type = "DATE" 
	)
	private Date baja;
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
		name = "codigouuid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String codigouuid;
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
		label = "correo",
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
		label = "correo",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
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
		label = "correo",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
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
		label = "Departamento",
		type = "VARCHAR" 
	)
	private String departamento;
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
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estado;
	@Column (
		name = "excludecampaing",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean excludecampaing;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "facebook",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String facebook;
	@Column (
		name = "fechacontacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Última Modificación",
		type = "DATE" 
	)
	private Date fechacontacto;
	@Column (
		name = "fecnacimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecnacimiento;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idmaillist",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idmaillist;
	@Id
	@Column (
		name = "idxcrmcontacto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxcrmcontacto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "linkedln",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String linkedln;
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
		label = "movil",
		type = "VARCHAR" 
	)
	private String movil;
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
		label = "nombre",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pais",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String pais;
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
		label = "telefono",
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
		max = 100 
	)
	@Column (
		name = "twitter",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String twitter;
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
		name = "IDRESPONSABLE0",
		referencedColumnName = "IDXCRMCONTACTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmcontacto idresponsable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMORIGEN0",
		referencedColumnName = "IDXMORIGEN",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Morigen idmorigen;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMCARGO0",
		referencedColumnName = "IDXMCARGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mcargo idmcargo;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Crmllamada> subcrmllamada;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Crmnota> subcrmnota;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idresponsable" 
	)
	private List<Crmcontacto> subcrmcontacto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Crmrcontacempresa> subcrmrcontacempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Crmtarea> subcrmtarea;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Mktcorreo> submktcorreo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Erpmgasto> suberpmgasto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Erpmproveedor> suberpmproveedor;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Mktrlistcustomer> submktrlistcustomer;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcontacto" 
	)
	private List<Citmagenda> subcitmagenda; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Crmcontacto getIdresponsable() {
		if(this.idresponsable==null)this.idresponsable=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idresponsable; 
	}
	
	public Morigen getIdmorigen() {
		if(this.idmorigen==null)this.idmorigen=new org.suinsit.apps.admin.Morigen();
		  return this.idmorigen; 
	}
	
	public Mcargo getIdmcargo() {
		if(this.idmcargo==null)this.idmcargo=new org.suinsit.apps.admin.Mcargo();
		  return this.idmcargo; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public List<Crmllamada> getSubcrmllamada() {
		if(this.subcrmllamada==null)this.subcrmllamada=new ArrayList<>(0);
		  return this.subcrmllamada; 
	}
	
	public List<Crmnota> getSubcrmnota() {
		if(this.subcrmnota==null)this.subcrmnota=new ArrayList<>(0);
		  return this.subcrmnota; 
	}
	
	public List<Crmcontacto> getSubcrmcontacto() {
		if(this.subcrmcontacto==null)this.subcrmcontacto=new ArrayList<>(0);
		  return this.subcrmcontacto; 
	}
	
	public List<Crmrcontacempresa> getSubcrmrcontacempresa() {
		if(this.subcrmrcontacempresa==null)this.subcrmrcontacempresa=new ArrayList<>(0);
		  return this.subcrmrcontacempresa; 
	}
	
	public List<Crmtarea> getSubcrmtarea() {
		if(this.subcrmtarea==null)this.subcrmtarea=new ArrayList<>(0);
		  return this.subcrmtarea; 
	}
	
	public List<Mktcorreo> getSubmktcorreo() {
		if(this.submktcorreo==null)this.submktcorreo=new ArrayList<>(0);
		  return this.submktcorreo; 
	}
	
	public List<Erpmgasto> getSuberpmgasto() {
		if(this.suberpmgasto==null)this.suberpmgasto=new ArrayList<>(0);
		  return this.suberpmgasto; 
	}
	
	public List<Erpmproveedor> getSuberpmproveedor() {
		if(this.suberpmproveedor==null)this.suberpmproveedor=new ArrayList<>(0);
		  return this.suberpmproveedor; 
	}
	
	public List<Mktrlistcustomer> getSubmktrlistcustomer() {
		if(this.submktrlistcustomer==null)this.submktrlistcustomer=new ArrayList<>(0);
		  return this.submktrlistcustomer; 
	}
	
	public List<Citmagenda> getSubcitmagenda() {
		if(this.subcitmagenda==null)this.subcitmagenda=new ArrayList<>(0);
		  return this.subcitmagenda; 
	} 

}