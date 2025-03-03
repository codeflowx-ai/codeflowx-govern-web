package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.List;
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
	name = "RAGAUDIT" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGAUDIT",
	labelMonitor = "RAG_AUDIT",
	pk = "idxragaudit" 
)
public class ComSuinsitAppsSuinlessRagaudit implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxragaudit",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragaudit;
	@NotNull
	@NotBlank
	@Column (
		name = "fecha",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fecha;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 200 
	)
	@Column (
		name = "tipoaccion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LIST_STRING" 
	)
	private List tipoaccion;
	@Column (
		name = "detalles",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String detalles;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ip",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String ip;
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
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String useragent;
	private boolean updatable; 

}