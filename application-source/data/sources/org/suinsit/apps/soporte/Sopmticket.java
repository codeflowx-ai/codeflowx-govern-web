package org.suinsit.apps.soporte;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.suinsit.apps.atlas.Atlhosting;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.atlas.Atlrproject;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.soporte.Sopmcriticidad;
import org.suinsit.apps.soporte.Sopmestado;
import org.suinsit.apps.soporte.Sopmtipo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SOPMTICKET" 
)
@Entidad (
	namespace = "soporte",
	type = "TABLE",
	name = "SOPMTICKET",
	labelMonitor = "CODSOPORTE",
	pk = "idxsopmticket" 
)
public class Sopmticket implements Serializable { 

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
	@Column (
		name = "cierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp cierre;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codsoporte",
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
		name = "SOPMTICKET_CODSOPORTE",
		prefix = "ST",
		mask = "0000000000",
		addYear = true 
	)
	private String codsoporte;
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
		name = "diagnostico",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String diagnostico;
	@Id
	@Column (
		name = "idxsopmticket",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsopmticket;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "inicidencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String inicidencia;
	@Column (
		name = "resolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String resolucion;
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
	private Crmempresa idcrmempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDINFORMANTE0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idinformante;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSOPMTIPO0",
		referencedColumnName = "IDXSOPMTIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sopmtipo idsopmtipo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSOPMCRITICIDAD0",
		referencedColumnName = "IDXSOPMCRITICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sopmcriticidad idsopmcriticidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSOPMESTADO0",
		referencedColumnName = "IDXSOPMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sopmestado idsopmestado;
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
		name = "IDATLPROJECT0",
		referencedColumnName = "IDXATLPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproject idatlproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLHOSTING0",
		referencedColumnName = "IDXATLHOSTING",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlhosting idatlhosting;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLRPROJECT0",
		referencedColumnName = "IDXATLRPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlrproject idatlrproject; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdinformante() {
		if(this.idinformante==null)this.idinformante=new org.suinsit.apps.admin.Ssousuario();
		  return this.idinformante; 
	}
	
	public Sopmtipo getIdsopmtipo() {
		if(this.idsopmtipo==null)this.idsopmtipo=new org.suinsit.apps.soporte.Sopmtipo();
		  return this.idsopmtipo; 
	}
	
	public Sopmcriticidad getIdsopmcriticidad() {
		if(this.idsopmcriticidad==null)this.idsopmcriticidad=new org.suinsit.apps.soporte.Sopmcriticidad();
		  return this.idsopmcriticidad; 
	}
	
	public Sopmestado getIdsopmestado() {
		if(this.idsopmestado==null)this.idsopmestado=new org.suinsit.apps.soporte.Sopmestado();
		  return this.idsopmestado; 
	}
	
	public Ssousuario getIdasignado() {
		if(this.idasignado==null)this.idasignado=new org.suinsit.apps.admin.Ssousuario();
		  return this.idasignado; 
	}
	
	public Atlproject getIdatlproject() {
		if(this.idatlproject==null)this.idatlproject=new org.suinsit.apps.atlas.Atlproject();
		  return this.idatlproject; 
	}
	
	public Atlhosting getIdatlhosting() {
		if(this.idatlhosting==null)this.idatlhosting=new org.suinsit.apps.atlas.Atlhosting();
		  return this.idatlhosting; 
	}
	
	public Atlrproject getIdatlrproject() {
		if(this.idatlrproject==null)this.idatlrproject=new org.suinsit.apps.atlas.Atlrproject();
		  return this.idatlrproject; 
	} 

}