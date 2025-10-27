package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSORACTIVIDAD" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSORACTIVIDAD",
	labelMonitor = "",
	pk = "idxssoractividad" 
)
public class Ssoractividad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "valuetupla",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String valuetupla;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "taskscreen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String taskscreen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "task",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String task;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "process",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String process;
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
		name = "aplicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String aplicacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dirip",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dirip;
	@Column (
		name = "idtupla",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer idtupla;
	@Id
	@Column (
		name = "idxssoractividad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssoractividad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "modulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String modulo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "username",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String username;
	private boolean updatable; 

}