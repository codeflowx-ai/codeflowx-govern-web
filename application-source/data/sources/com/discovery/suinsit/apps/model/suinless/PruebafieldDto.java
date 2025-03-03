package com.discovery.suinsit.apps.model.suinless;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Byte;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "suinless",
	name = "PRUEBAFIELD" 
)
@Schema
public class PruebafieldDto { 

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
	private Map mapstring;
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
	private Map mapobjetos;
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
	private List listafields;
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
	private Byte[] vectorfield; 

}