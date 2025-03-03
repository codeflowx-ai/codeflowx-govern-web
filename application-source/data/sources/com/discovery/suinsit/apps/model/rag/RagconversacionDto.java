package com.discovery.suinsit.apps.model.rag;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
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
	name = "RAGCONVERSACION" 
)
@Schema (
	name = "RAG_CONVERSACION" 
)
public class RagconversacionDto { 

	@Schema (
		maxLength = 16 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal costecalculado;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String descripcion;
	@Schema (
		maxLength = 1535 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 1535 
	)
	private List estado;
	@Schema
	private LocalDate fechafin;
	@Schema
	private LocalDate fechainicio;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String feedbackusuario;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String idioma;
	@Schema (
		maxLength = 1535 
	)
	@Size (
		min = 0,
		max = 1535 
	)
	private List modelo;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String sessionid;
	@Schema (
		maxLength = 1535 
	)
	@Size (
		min = 0,
		max = 1535 
	)
	private List tags;
	@Schema (
		maxLength = 100 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String titulo;
	@Schema
	private LocalDate ultimaactividad;
	@Schema
	private Integer valoracionusuario; 

}