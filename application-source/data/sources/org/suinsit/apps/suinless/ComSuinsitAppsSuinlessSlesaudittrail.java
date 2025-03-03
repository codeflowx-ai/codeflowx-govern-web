package org.suinsit.apps.suinless;

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
	name = "SLESAUDITTRAIL" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAUDITTRAIL",
	labelMonitor = "AUDIT_TRAIL",
	pk = "idxslesaudittrail" 
)
public class ComSuinsitAppsSuinlessSlesaudittrail implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesaudittrail",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesaudittrail;
	@NotNull
	@NotBlank
	@Column (
		name = "eventdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp eventdate;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 50 
	)
	@Column (
		name = "eventtype",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String eventtype;
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
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String userid;
	@Column (
		name = "requestdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String requestdata;
	@Column (
		name = "responsedata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String responsedata;
	@Column (
		name = "metadata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String metadata;
	private boolean updatable; 

}