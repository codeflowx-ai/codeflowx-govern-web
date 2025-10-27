package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
	name = "SSOMRKEY" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOMRKEY",
	labelMonitor = "",
	pk = "idxsso_mrkey" 
)
public class Ssomrkey implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "vencimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp vencimiento;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@Column (
		name = "privkey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object privkey;
	@Column (
		name = "pubkey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object pubkey;
	@Size (
		min = 0,
		max = 1000 
	)
	@Column (
		name = "uuid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR",
		encrypt = true 
	)
	private String uuid;
	@Id
	@Column (
		name = "idxsso_mrkey",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsso_mrkey;
	private boolean updatable; 

}