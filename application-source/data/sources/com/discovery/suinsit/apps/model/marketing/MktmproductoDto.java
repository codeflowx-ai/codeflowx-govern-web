package com.discovery.suinsit.apps.model.marketing;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "marketing",
	name = "MKTMPRODUCTO" 
)
@Schema (
	name = "MKTMPRODUCTO" 
)
public class MktmproductoDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean other;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean suincode;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean suinisit;
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
	private String producto; 

}