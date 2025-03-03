package com.discovery.suinsit.apps.model.crm;

import com.discovery.suinsit.apps.model.crm.Crmpipeline;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "crm",
	name = "CRMETAPA" 
)
@Schema (
	name = "Etapa" 
)
public class CrmetapaDto { 

	@Schema (
		description = "etapa de cierre",
		example = "true/false" 
	)
	private boolean cierre;
	@JsonProperty (
		value = "uuid" 
	)
	@Schema (
		maxLength = 100,
		description = "codigo interno uuid",
		example = "3e6442b4-83dc-4299-b3ec-dd37eae70bdf" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String coduuid;
	@JsonProperty (
		value = "etapa" 
	)
	@Schema (
		maxLength = 100,
		description = "Nombre de la etapa",
		example = "propspección" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String etapa;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean ganada;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean perdida;
	@Schema (
		maxLength = 16,
		description = "porcentaje de éxito" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal porcentaje;
	@JsonProperty (
		value = "pipeline" 
	)
	@Valid
	@Schema (
		name = "pipeline" 
	)
	private Crmpipeline idcrmpipeline; 

}