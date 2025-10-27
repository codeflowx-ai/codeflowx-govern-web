package com.discovery.suinsit.apps.model.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
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
public class MtipotaskDto { 

	@Schema (
		maxLength = 100,
		description = "Tipo de tarea" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String tipotarea; 

}