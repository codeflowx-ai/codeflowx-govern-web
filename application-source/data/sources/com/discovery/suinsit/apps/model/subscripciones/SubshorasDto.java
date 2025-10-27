package com.discovery.suinsit.apps.model.subscripciones;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
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
	namespace = "subscripciones",
	name = "SUBSHORAS" 
)
@Schema (
	name = "Horas" 
)
public class SubshorasDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String concepto;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private String descripcion;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private LocalDate fecha;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal tiempo; 

}