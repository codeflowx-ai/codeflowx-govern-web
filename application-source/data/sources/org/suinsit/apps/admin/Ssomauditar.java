package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
	name = "SSOMAUDITAR" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOMAUDITAR",
	labelMonitor = "",
	pk = "idxssomauditar" 
)
public class Ssomauditar implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "value",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String value;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "field",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String field;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "module",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String module;
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
	@Id
	@Column (
		name = "idxssomauditar",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssomauditar;
	private boolean updatable; 

}