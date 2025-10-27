package com.discovery.suinsit.apps.model.subvenciones;

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
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "subvenciones",
	name = "KITRHORASCAT" 
)
@Schema (
	name = "horas" 
)
public class KitrhorascatDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,JUSTIFICACION,SUBSANACION,ASISTENCIA,OTROS" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",JUSTIFICACION,SUBSANACION,ASISTENCIA,OTROS" 
		},
		message = "solamente admite lo valores: ,JUSTIFICACION,SUBSANACION,ASISTENCIA,OTROS" 
	)
	private String motivo;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	@NotNull
	@NotBlank
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
	private BigDecimal horas;
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

}