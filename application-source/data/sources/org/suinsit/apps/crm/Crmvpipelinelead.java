package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "crm",
	type = "VIEW",
	name = "CRMVPIPELINELEAD" 
)
public class Crmvpipelinelead implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Column (
		name = "defecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Principal",
		type = "BOOLEAN" 
	)
	private boolean defecto;
	@Column (
		name = "idxcrmpipeline",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmpipeline;
	@Column (
		name = "maxreg",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Max. Registros",
		type = "INTEGER" 
	)
	private Integer maxreg;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",CRMOPORTUNIDAD,CRMTASK" 
		},
		message = "solamente admite lo valores: ,CRMOPORTUNIDAD,CRMTASK" 
	)
	@Column (
		name = "modulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Objecto",
		type = "ENUM_STRING" 
	)
	private String modulo;
	@Column (
		name = "ordenasc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Ordenar ascendentemente",
		type = "BOOLEAN" 
	)
	private boolean ordenasc;
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