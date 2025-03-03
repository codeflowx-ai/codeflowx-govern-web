package com.discovery.suinsit.apps.model.rag;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.time.LocalDate;
import java.util.List;
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
	namespace = "rag",
	name = "RAGAUDIT" 
)
@Schema (
	name = "RAG_AUDIT" 
)
public class RagauditDto { 

	@Schema
	@NotNull
	@NotBlank
	private LocalDate fecha;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String ip;
	@Schema (
		maxLength = 1535 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 1535 
	)
	private List tipoaccion;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String useragent; 

}