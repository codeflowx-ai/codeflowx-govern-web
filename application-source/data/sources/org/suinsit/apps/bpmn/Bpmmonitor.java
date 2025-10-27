package org.suinsit.apps.bpmn;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	name = "BPMMONITOR" 
)
@Entidad (
	namespace = "bpmn",
	type = "TABLE",
	name = "BPMMONITOR",
	labelMonitor = "",
	pk = "idxbpmmonitor" 
)
public class Bpmmonitor implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "bussinesmod",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String bussinesmod;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "instanceprocess",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String instanceprocess;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "instancetask",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String instancetask;
	@Column (
		name = "finish",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp finish;
	@Column (
		name = "start",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp start;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nametask",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nametask;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "taskey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String taskey;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "processkey",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String processkey;
	@Id
	@Column (
		name = "idxbpmmonitor",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxbpmmonitor;
	private boolean updatable; 

}