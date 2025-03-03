package com.discovery.suinsit.apps.model.crm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.FieldType;
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Schema (
	name = "Direccion" 
)
public class CrmdireccionDto { 

	@JsonProperty (
		value = "ciudad" 
	)
	@Schema (
		maxLength = 100,
		description = "Ciudad",
		example = "Madrid" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String ciudad;
	@JsonProperty (
		value = "email" 
	)
	@Schema (
		maxLength = 100,
		description = "correo de la dirección",
		example = "micorreo@micorreo.com" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String correo;
	@JsonProperty (
		value = "direccion" 
	)
	@Schema (
		maxLength = 100,
		description = "Dirección",
		example = "mi dirección" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String direccion;
	@Schema (
		maxLength = 100 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String estado;
	@JsonProperty (
		value = "movil" 
	)
	@Schema (
		maxLength = 100,
		description = "numero de movil",
		example = "+34652478987" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String movil;
	@Schema (
		maxLength = 100,
		description = "teléfono",
		example = "+3497854782" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String telefono;
	@Schema (
		maxLength = 150,
		description = "Tipo de dirección" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Facturación,Entrega,Otra" 
		},
		message = "nullsolamente admite lo valores: ,Facturación,Entrega,Otra" 
	)
	private String tipo; 

}