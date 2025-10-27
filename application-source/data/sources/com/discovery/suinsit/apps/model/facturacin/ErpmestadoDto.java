package com.discovery.suinsit.apps.model.facturacin;

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
	namespace = "facturacin",
	name = "ERPMESTADO" 
)
@Schema (
	name = "Estado" 
)
public class ErpmestadoDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean remove;
	@Schema (
		maxLength = 150 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark,bg-danger" 
		},
		message = "nullsolamente admite lo valores: ,bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark,bg-danger" 
	)
	private String bgcolor;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean pedido;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean presupuesto;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,CREACION,ENVIO,ANUALADA,CANCELADA,ACEPTADA,PENDIENTE" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",CREACION,ENVIO,ANUALADA,CANCELADA,ACEPTADA,PENDIENTE" 
		},
		message = "solamente admite lo valores: ,CREACION,ENVIO,ANUALADA,CANCELADA,ACEPTADA,PENDIENTE" 
	)
	private String whenaction; 

}