package com.discovery.suinsit.apps.model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema (
	name = "" 
)
public class SsoapikeyDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "accessKey",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String acceskey;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "activa",
		example = "true" 
	)
	private boolean active;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "alias",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String alias;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "fecha de alta",
		example = "" 
	)
	@NotNull
	@NotBlank
	private LocalDate alta;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "mi apy key",
		example = "" 
	)
	private String descripcion;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "fecha expiracion",
		example = "" 
	)
	private Timestamp expired;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "solo lectura",
		example = "" 
	)
	private boolean readonly;
	@JsonProperty (
		value = "secretkey" 
	)
	@Schema (
		maxLength = 100,
		description = "clave secret",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String secretkey; 

}