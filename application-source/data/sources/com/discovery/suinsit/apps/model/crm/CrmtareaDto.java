package com.discovery.suinsit.apps.model.crm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema (
	name = "Tarea" 
)
public class CrmtareaDto { 

	@Schema (
		description = "Nota" 
	)
	private String nota;
	@Schema (
		description = "flag si debe de enviar reocrdatorio" 
	)
	private boolean recordatorio;
	@JsonProperty (
		value = "task" 
	)
	@Schema (
		maxLength = 100,
		description = "nombre de la tarea" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String tarea;
	@JsonProperty (
		value = "duedate" 
	)
	@Schema (
		description = "fecha vencimiento" 
	)
	@NotNull
	@NotBlank
	private Timestamp vencimiento; 

}