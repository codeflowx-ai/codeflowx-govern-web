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
	name = "AUDITLOG" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "AUDITLOG",
	pk = "idxauditlog" 
)
public class Auditlog implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "newvalue",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String newvalue;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "previousvalue",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String previousvalue;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "useragent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String useragent;
	@Column (
		name = "timestamp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp timestamp;
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
		name = "entitytype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String entitytype;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "entityid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String entityid;
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
		name = "idxauditlog",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxauditlog;
	private boolean updatable; 

}