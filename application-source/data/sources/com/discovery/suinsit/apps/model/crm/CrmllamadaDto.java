package com.discovery.suinsit.apps.model.crm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema (
	name = "Llamada" 
)
public class CrmllamadaDto { 

	@JsonProperty (
		value = "comment" 
	)
	@Schema (
		description = "comentarios" 
	)
	private String comentarios;
	@JsonProperty (
		value = "datecall" 
	)
	@Schema (
		description = "fecha realización" 
	)
	@NotNull
	@NotBlank
	private Timestamp realizada; 

}