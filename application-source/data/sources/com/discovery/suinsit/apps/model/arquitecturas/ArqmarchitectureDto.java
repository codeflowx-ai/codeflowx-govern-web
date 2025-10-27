package com.discovery.suinsit.apps.model.arquitecturas;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Object;
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
	namespace = "arquitecturas",
	name = "ARQMARCHITECTURE" 
)
@Schema
public class ArqmarchitectureDto { 

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
	private String autor;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean activo;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private LocalDate actualizacion;
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
	private String arquitectura;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Object avatar;
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
	private String basepaquete;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal ctepartner;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal cteproyecto;
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
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String descshort;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean genleka;
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
	private String handlerclass;
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
	private String icono;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal licencia;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean multicode;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean opensource;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private String resumen;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean showdesktop;
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
	private String subpath;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean useleka;
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
	private String version; 

}