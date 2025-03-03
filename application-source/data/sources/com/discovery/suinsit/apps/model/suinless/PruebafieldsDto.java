package com.discovery.suinsit.apps.model.suinless;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "suinless",
	name = "PRUEBAFIELDS" 
)
@Schema
public class PruebafieldsDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 1535,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 1535 
	)
	private List listastring; 

}