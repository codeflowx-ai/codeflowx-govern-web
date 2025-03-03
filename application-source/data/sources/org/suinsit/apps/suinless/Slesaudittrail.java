package org.suinsit.apps.suinless;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slesmodel;

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
public class Slesaudittrail implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesaudittrail",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
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
		max = 100 
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel; 

	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	} 

}