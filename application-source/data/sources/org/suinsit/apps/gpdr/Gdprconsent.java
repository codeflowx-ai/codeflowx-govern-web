package org.suinsit.apps.gpdr;

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
	name = "GDPRCONSENT" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "GDPRCONSENT",
	pk = "idxgdprconsent" 
)
public class Gdprconsent implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "consentdate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp consentdate;
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
	@Column (
		name = "active",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean active;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "consenttype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String consenttype;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "consentversion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String consentversion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "datacategories",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String datacategories;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "datarecipients",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String datarecipients;
	@Column (
		name = "granted",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean granted;
	@Id
	@Column (
		name = "idxgdprconsent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgdprconsent;
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
		name = "legalbasis",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String legalbasis;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "purposedescription",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String purposedescription;
	@Column (
		name = "retentionperiodmonths",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer retentionperiodmonths;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "useremail",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String useremail;
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
	private boolean updatable; 

}