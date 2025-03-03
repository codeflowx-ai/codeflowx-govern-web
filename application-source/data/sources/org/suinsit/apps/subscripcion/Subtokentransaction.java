package org.suinsit.apps.subscripcion;

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
	name = "SUBTOKENTRANSACTION" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBTOKENTRANSACTION",
	pk = "idxsubtokentransaction" 
)
public class Subtokentransaction implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "transatdate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp transatdate;
	@Column (
		name = "tokenout",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer tokenout;
	@Column (
		name = "tokenin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer tokenin;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "typetransaction",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String typetransaction;
	@Column (
		name = "usedtoken",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer usedtoken;
	@Id
	@Column (
		name = "idxsubtokentransaction",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubtokentransaction;
	private boolean updatable; 

}