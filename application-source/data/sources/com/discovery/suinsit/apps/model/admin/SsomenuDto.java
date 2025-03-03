package com.discovery.suinsit.apps.model.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
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
	namespace = "admin",
	name = "SSOMENU" 
)
@Schema (
	name = "MENU" 
)
public class SsomenuDto { 

	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String dashboardpage;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String descripcion;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String icono;
	@Schema (
		maxLength = 100 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String menu;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String namespace;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String title; 

}