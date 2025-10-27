package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
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
@Entidad (
	namespace = "crm",
	type = "VIEW",
	name = "CRMVLEADS" 
)
public class Crmvleads implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "etapa",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Etapa",
		type = "VARCHAR" 
	)
	private String etapa;
	@NotNull
	@NotBlank
	@Column (
		name = "orden",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "orden",
		type = "INTEGER" 
	)
	private Integer orden;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pipeline",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Pipeline",
		type = "VARCHAR" 
	)
	private String pipeline;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Descripción",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "fectstart",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha prevista",
		type = "DATE" 
	)
	private Date fectstart;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importeprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Importe Previsto",
		type = "DECIMAL" 
	)
	private BigDecimal importeprev;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "oportunidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "oportunidad",
		type = "VARCHAR" 
	)
	private String oportunidad;
	@Column (
		name = "position",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer position;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "probabilidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal probabilidad;
	@Column (
		name = "idcrmpipeline0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idcrmpipeline0;
	@Column (
		name = "idcrmetapa0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idcrmetapa0;
	@Column (
		name = "idxoportunidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxoportunidad;
	private boolean updatable; 

}