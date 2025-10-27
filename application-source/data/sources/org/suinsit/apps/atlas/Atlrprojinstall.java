package org.suinsit.apps.atlas;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.atlas.Atlproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLRPROJINSTALL" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRPROJINSTALL",
	labelMonitor = "",
	pk = "idxatlrprojinstall" 
)
public class Atlrprojinstall implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 2000 
	)
	@Column (
		name = "mensaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String mensaje;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "accion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String accion;
	@Column (
		name = "alta",
		nullable = true 
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
		name = "cluster",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cluster;
	@Id
	@Column (
		name = "idxatlrprojinstall",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrprojinstall;
	@Column (
		name = "instalacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean instalacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namespace;
	@Column (
		name = "reinicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean reinicio;
	private boolean updatable;
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

	public Atlproject getIdatlproject() {
		if(this.idatlproject==null)this.idatlproject=new org.suinsit.apps.atlas.Atlproject();
		  return this.idatlproject; 
	} 

}