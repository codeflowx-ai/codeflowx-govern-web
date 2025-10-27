package com.discovery.suinsit.apps.model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Object;
import java.lang.String;
import java.sql.Timestamp;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "admin",
	name = "SSOMRKEY" 
)
@Schema (
	name = "" 
)
public class SsomrkeyDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Timestamp vencimiento;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Timestamp alta;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Object privkey;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Object pubkey;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 1000,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 1000 
	)
	private String uuid; 

}