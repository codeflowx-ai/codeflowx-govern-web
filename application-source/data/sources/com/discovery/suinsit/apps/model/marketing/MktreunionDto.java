package com.discovery.suinsit.apps.model.marketing;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "marketing",
	name = "MKTREUNION" 
)
@Schema (
	name = "MKTREUNION" 
)
public class MktreunionDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private String comentarios;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Timestamp fechahora;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,POSITIVO,NEGATIVO,NEUTRO" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",POSITIVO,NEGATIVO,NEUTRO" 
		},
		message = "solamente admite lo valores: ,POSITIVO,NEGATIVO,NEUTRO" 
	)
	private String resultado;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,DEMO,PRIMER CONTACTO,SEGUIMIENTO,OTROS" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",DEMO,PRIMER CONTACTO,SEGUIMIENTO,OTROS" 
		},
		message = "solamente admite lo valores: ,DEMO,PRIMER CONTACTO,SEGUIMIENTO,OTROS" 
	)
	private String tipo; 

}