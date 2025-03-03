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
	name = "PRIVACYPOLICYVERSION" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "PRIVACYPOLICYVERSION",
	pk = "idxprivacypolicyversion" 
)
public class Privacypolicyversion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "effectivedate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp effectivedate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "changelog",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String changelog;
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
	@Column (
		name = "content",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String content;
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
	@Id
	@Column (
		name = "idxprivacypolicyversion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxprivacypolicyversion;
	private boolean updatable; 

}