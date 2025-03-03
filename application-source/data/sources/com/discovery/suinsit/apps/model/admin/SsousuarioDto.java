package com.discovery.suinsit.apps.model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Object;
import java.lang.String;
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
	namespace = "admin",
	name = "SSOUSUARIO" 
)
@Schema
public class SsousuarioDto { 

	@Schema (
		description = "Fecha de alta" 
	)
	private LocalDate alta;
	@Schema (
		maxLength = 100,
		description = "apellidos" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String apellidos;
	@Schema (
		description = "fotografia o avatar" 
	)
	private Object avatar;
	@Schema (
		description = "flag baja de usuario" 
	)
	private LocalDate baja;
	@Schema (
		maxLength = 100,
		description = "correo electrónico" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String email;
	@Schema (
		description = "flag para bloquear usuario" 
	)
	private boolean locked;
	@Schema (
		maxLength = 100,
		description = "nombre" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String nombre;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean sendacceslog;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean showdesktop;
	@Schema (
		maxLength = 150,
		description = "Tipo de usuario" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Aplicacion,Portales,Sistema,Portal empleado" 
		},
		message = "nullsolamente admite lo valores: ,Aplicacion,Portales,Sistema,Portal empleado" 
	)
	private String tipousuario;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean twofactor;
	@JsonProperty (
		value = "username" 
	)
	@Schema (
		maxLength = 100,
		description = "Nombre de usuario" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String usuario;
	@JsonProperty (
		value = "b924ed30-f1f6-48c1-9fe4-6183b55569d3" 
	)
	@Schema (
		maxLength = 100,
		description = "user code",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String uuidcode; 

}