package com.discovery.suinsit.apps.model.document;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
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
	namespace = "document",
	name = "DOCRLINKS" 
)
@Schema
public class DocrlinksDto { 

	@Schema
	@NotNull
	@NotBlank
	private LocalDate alta;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String couseralta;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String cousermodif;
	@Schema (
		maxLength = 100 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String enlace;
	@Schema
	@NotNull
	@NotBlank
	private LocalDate fin;
	@Schema
	private Timestamp tmalta;
	@Schema
	private Timestamp tmmodif;
	@Schema
	private Timestamp ultimadescarga; 

}