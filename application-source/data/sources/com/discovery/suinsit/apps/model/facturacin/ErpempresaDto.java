package com.discovery.suinsit.apps.model.facturacin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.FieldType;
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "facturacin",
	name = "ERPEMPRESA" 
)
@Schema (
	name = "Empresa" 
)
public class ErpempresaDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal retencion;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 16,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal recargo;
	@Schema (
		maxLength = 100 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String ciudad;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String codpostal;
	@Schema (
		maxLength = 100 
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String email;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String folio;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String registro;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,Residente,Residente UE,Extranjero" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Residente,Residente UE,Extranjero" 
		},
		message = "solamente admite lo valores: ,Residente,Residente UE,Extranjero" 
	)
	private String residente;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String sheet;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,Juridica,Fisica" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Juridica,Fisica" 
		},
		message = "solamente admite lo valores: ,Juridica,Fisica" 
	)
	private String tipo;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 100,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String volumen; 

}