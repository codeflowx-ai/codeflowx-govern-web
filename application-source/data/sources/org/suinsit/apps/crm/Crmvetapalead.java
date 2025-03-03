package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
	name = "CRMVETAPALEAD" 
)
public class Crmvetapalead implements Serializable { 

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
	@Column (
		name = "idxcrmetapa",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmetapa;
	@Column (
		name = "mostrarmotiv",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mostrarmotiv;
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
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "porcentaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Porcentaje (%)",
		type = "DECIMAL" 
	)
	private BigDecimal porcentaje;
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
	private boolean updatable; 

}