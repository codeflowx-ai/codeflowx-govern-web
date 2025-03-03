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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "DATAEXPORTREQUEST" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "DATAEXPORTREQUEST",
	pk = "idxdataexportrequest" 
)
public class Dataexportrequest implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "expirydate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp expirydate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "downloadurl",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String downloadurl;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "format",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String format;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String status;
	@Column (
		name = "completiondate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp completiondate;
	@Column (
		name = "requestdate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp requestdate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "requestid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String requestid;
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
	@AutoGenerate (
		uuid = true 
	)
	private String userid;
	@Id
	@Column (
		name = "idxdataexportrequest",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxdataexportrequest;
	private boolean updatable; 

}