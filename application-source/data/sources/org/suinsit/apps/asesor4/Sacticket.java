package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.Random;
import org.suinsit.apps.admin.Mtipoprioridad;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmroporticket;
import org.suinsit.apps.crm.Crmtarea;
import org.suinsit.apps.document.Docfichero;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SACTICKET" 
)
@Entidad (
	namespace = "asesor4",
	type = "TABLE",
	name = "SACTICKET",
	pk = "idxticket" 
)
public class Sacticket implements Serializable { 

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
		label = "alta",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "cierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date cierre;
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
	@Id
	@Column (
		name = "idxticket",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxticket;
	@Column (
		name = "modificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date modificacion;
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
	@Column (
		name = "recordatorio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Recordatorio",
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
		name = "referencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "referencia",
		type = "VARCHAR" 
	)
	@Random (
		lengh = 10,
		prefix = "null",
		type = "both" 
	)
	private String referencia;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ticket",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "ticket",
		type = "VARCHAR" 
	)
	private String ticket;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMTAREA0",
		referencedColumnName = "IDXCRMTAREA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtarea idcrmtarea;
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
		name = "IDASIGNADO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idasignado;
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
		mappedBy = "idsacticket" 
	)
	private List<Crmroporticket> subcrmroporticket;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsacticket" 
	)
	private List<Docfichero> subdocfichero; 

	public Crmtarea getIdcrmtarea() {
		if(this.idcrmtarea==null)this.idcrmtarea=new org.suinsit.apps.crm.Crmtarea();
		  return this.idcrmtarea; 
	}
	
	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Mtipoprioridad getIdmtipoprioridad() {
		if(this.idmtipoprioridad==null)this.idmtipoprioridad=new org.suinsit.apps.admin.Mtipoprioridad();
		  return this.idmtipoprioridad; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdasignado() {
		if(this.idasignado==null)this.idasignado=new org.suinsit.apps.admin.Ssousuario();
		  return this.idasignado; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public List<Crmroporticket> getSubcrmroporticket() {
		if(this.subcrmroporticket==null)this.subcrmroporticket=new ArrayList<>(0);
		  return this.subcrmroporticket; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	} 

}