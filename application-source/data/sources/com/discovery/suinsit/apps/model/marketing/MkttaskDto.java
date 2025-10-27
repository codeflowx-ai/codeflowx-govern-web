package com.discovery.suinsit.apps.model.marketing;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "marketing",
	name = "MKTTASK" 
)
@Schema (
	name = "MKTTASK" 
)
public class MkttaskDto { 

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
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String tarea; 

}