package com.discovery.suinsit.apps.model.atlas;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "atlas",
	name = "ATLESTADOS" 
)
@Schema (
	name = "ESTADOS" 
)
public class AtlestadosDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,bg-primary,bg-sucess,bg-info,bg-warning,bg-danger,bg-dark,bg-secondary,bg-light" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",bg-primary,bg-sucess,bg-info,bg-warning,bg-danger,bg-dark,bg-secondary,bg-light" 
		},
		message = "solamente admite lo valores: ,bg-primary,bg-sucess,bg-info,bg-warning,bg-danger,bg-dark,bg-secondary,bg-light" 
	)
	private String bgcolor;
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
	private String estado; 

}