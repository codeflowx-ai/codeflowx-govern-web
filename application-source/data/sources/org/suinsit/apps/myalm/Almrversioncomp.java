package org.suinsit.apps.myalm;

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
	name = "ALMRVERSIONCOMP" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRVERSIONCOMP",
	labelMonitor = "VERSION",
	pk = "idxalmrversioncomp" 
)
public class Almrversioncomp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "dploymetyml",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String dploymetyml;
	@Id
	@Column (
		name = "idxalmrversioncomp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrversioncomp;
	@Column (
		name = "latest",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean latest;
	@Column (
		name = "publicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp publicacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tag;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	private boolean updatable; 

}