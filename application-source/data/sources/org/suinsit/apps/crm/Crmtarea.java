package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.admin.Mtipoprioridad;
import org.suinsit.apps.admin.Mtipotask;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.asesor4.Sacticket;
import org.suinsit.apps.crm.Crmcolatask;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmhistetapas;
import org.suinsit.apps.crm.Crmllamada;
import org.suinsit.apps.crm.Crmnota;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmropotask;
import org.suinsit.apps.crm.Crmtaskperiodo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMTAREA" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMTAREA",
	labelMonitor = "TAREA",
	pk = "idxcrmtarea" 
)
public class Crmtarea implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxcrmtarea",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmtarea;
	@Column (
		name = "nota",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Nota",
		type = "CLOB" 
	)
	private String nota;
	@Column (
		name = "recordatorio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "recordatorio",
		type = "BOOLEAN" 
	)
	private boolean recordatorio;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tarea",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tarea",
		type = "VARCHAR" 
	)
	private String tarea;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
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
		type = "VARCHAR" 
	)
	private String tmmodif;
	@NotNull
	@NotBlank
	@Column (
		name = "vencimiento",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Vencimiento",
		type = "TIMESTAMP" 
	)
	private Timestamp vencimiento;
	private boolean updatable;
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
		name = "IDCRMCONTACTO0",
		referencedColumnName = "IDXCRMCONTACTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmcontacto idcrmcontacto;
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
		name = "IDCRMCOLATASK0",
		referencedColumnName = "IDXCRMCOLATASK",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmcolatask idcrmcolatask;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMTASKPERIODO0",
		referencedColumnName = "IDXCRMTASKPERIODO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtaskperiodo idcrmtaskperiodo;
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
		name = "IDMTIPOTASK0",
		referencedColumnName = "IDXTIPOTASK",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mtipotask idmtipotask;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtarea" 
	)
	private List<Sacticket> subsacticket;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtarea" 
	)
	private List<Crmropotask> subcrmropotask;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtarea" 
	)
	private List<Crmnota> subcrmnota;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtarea" 
	)
	private List<Crmhistetapas> subcrmhistetapas;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtarea" 
	)
	private List<Crmllamada> subcrmllamada; 

	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmcolatask getIdcrmcolatask() {
		if(this.idcrmcolatask==null)this.idcrmcolatask=new org.suinsit.apps.crm.Crmcolatask();
		  return this.idcrmcolatask; 
	}
	
	public Crmtaskperiodo getIdcrmtaskperiodo() {
		if(this.idcrmtaskperiodo==null)this.idcrmtaskperiodo=new org.suinsit.apps.crm.Crmtaskperiodo();
		  return this.idcrmtaskperiodo; 
	}
	
	public Mtipoprioridad getIdmtipoprioridad() {
		if(this.idmtipoprioridad==null)this.idmtipoprioridad=new org.suinsit.apps.admin.Mtipoprioridad();
		  return this.idmtipoprioridad; 
	}
	
	public Mtipotask getIdmtipotask() {
		if(this.idmtipotask==null)this.idmtipotask=new org.suinsit.apps.admin.Mtipotask();
		  return this.idmtipotask; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Sacticket> getSubsacticket() {
		if(this.subsacticket==null)this.subsacticket=new ArrayList<>(0);
		  return this.subsacticket; 
	}
	
	public List<Crmropotask> getSubcrmropotask() {
		if(this.subcrmropotask==null)this.subcrmropotask=new ArrayList<>(0);
		  return this.subcrmropotask; 
	}
	
	public List<Crmnota> getSubcrmnota() {
		if(this.subcrmnota==null)this.subcrmnota=new ArrayList<>(0);
		  return this.subcrmnota; 
	}
	
	public List<Crmhistetapas> getSubcrmhistetapas() {
		if(this.subcrmhistetapas==null)this.subcrmhistetapas=new ArrayList<>(0);
		  return this.subcrmhistetapas; 
	}
	
	public List<Crmllamada> getSubcrmllamada() {
		if(this.subcrmllamada==null)this.subcrmllamada=new ArrayList<>(0);
		  return this.subcrmllamada; 
	} 

}