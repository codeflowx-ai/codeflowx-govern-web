package org.suinsit.apps.gpdr;

import java.io.Serializable;
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
	name = "GDPRAUDITLOG" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "GDPRAUDITLOG",
	pk = "idxgdprauditlog" 
)
public class Gdprauditlog implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecha;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ipaddress",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ipaddress;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "performedby",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String performedby;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "details",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String details;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "action",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String action;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "consentid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String consentid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "userid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String userid;
	@Id
	@Column (
		name = "idxgdprauditlog",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgdprauditlog;
	private boolean updatable; 

}