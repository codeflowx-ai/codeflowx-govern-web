package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.sql.Date;
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
	name = "NEGOCIOS" 
)
@Entidad (
	namespace = "asesor4",
	type = "TABLE",
	name = "NEGOCIOS",
	pk = "idxnegocios" 
)
public class Negocios implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxnegocios",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer idxnegocios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String status;
	@Column (
		name = "position",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer position;
	@Column (
		name = "archived",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean archived;
	@Column (
		name = "fecstart",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "DATE" 
	)
	private Date fecstart;
	private boolean updatable; 

}