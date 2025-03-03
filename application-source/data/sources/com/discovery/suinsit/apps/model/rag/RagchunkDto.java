package com.discovery.suinsit.apps.model.rag;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "rag",
	name = "RAGCHUNK" 
)
@Schema (
	name = "RAG_CHUNK" 
)
public class RagchunkDto { 

	@Schema
	private String contenido;
	@Schema
	private LocalDate fechacreacion;
	@Schema
	private LocalDate fechamodificacion;
	@Schema
	private Integer fin;
	@Schema
	private Integer inicio;
	@Schema
	private Integer tokens; 

}