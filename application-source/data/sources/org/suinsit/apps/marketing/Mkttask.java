package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.marketing.Mktproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTTASK" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTTASK",
	labelMonitor = "TAREA",
	pk = "idxmkttask" 
)
public class Mkttask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
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
		name = "MKTTASK_REFERENCIA",
		prefix = "MKT",
		mask = "000000",
		addYear = true 
	)
	private String referencia;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tarea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tarea;
	@Id
	@Column (
		name = "idxmkttask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmkttask;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPROJECT0",
		referencedColumnName = "IDXMKTPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktproject idmktproject;
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
		name = "IDRESPONSABLE0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idresponsable; 

	public Mktproject getIdmktproject() {
		if(this.idmktproject==null)this.idmktproject=new org.suinsit.apps.marketing.Mktproject();
		  return this.idmktproject; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Ssousuario getIdresponsable() {
		if(this.idresponsable==null)this.idresponsable=new org.suinsit.apps.admin.Ssousuario();
		  return this.idresponsable; 
	} 

}