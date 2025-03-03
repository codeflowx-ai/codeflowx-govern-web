package com.discovery.suinsit.apps.model.citaprev;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Schema (
	name = "Proghramar" 
)
public class CitrusuhorarioDto { 

	@Schema (
		maxLength = 150 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
		},
		message = "nullsolamente admite lo valores: ,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
	)
	private String dias; 

}