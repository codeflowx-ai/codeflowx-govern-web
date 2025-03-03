package com.discovery.suinsit.apps.model.crm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.math.BigDecimal;
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
	namespace = "crm",
	name = "CRMPRODUCTO" 
)
@Schema (
	name = "Producto" 
)
public class CrmproductoDto { 

	@Schema (
		description = "producto activo",
		example = "true/false" 
	)
	@NotNull
	@NotBlank
	private boolean activo;
	@JsonProperty (
		value = "alta" 
	)
	@Schema (
		description = "fecha de alta del producto",
		example = "01/07/2022" 
	)
	@NotNull
	@NotBlank
	private LocalDate alta;
	@Schema (
		description = "baja del producto",
		example = "01/02/2023" 
	)
	private boolean baja;
	@JsonProperty (
		value = "uuid" 
	)
	@Schema (
		maxLength = 100,
		description = "uuid del producto",
		example = "194990f6-03b8-4096-a782-37ef027275da" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String coduuid;
	@Schema (
		maxLength = 16,
		description = "comision del producto",
		example = "25" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal comision;
	@Schema (
		description = "descripción del producto",
		example = "mi producto" 
	)
	private String descripcion;
	@Schema (
		maxLength = 16,
		description = "existencias del producto",
		example = "1000" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal existencias;
	@JsonProperty (
		value = "finvtas" 
	)
	@Schema (
		description = "fecha fin de ventas",
		example = "01/02/2023" 
	)
	private LocalDate ffinventas;
	@JsonProperty (
		value = "fecinicio" 
	)
	@Schema (
		description = "Fecha inicio de ventas",
		example = "01/03/2023" 
	)
	private LocalDate finicioventas;
	@JsonProperty (
		value = "precio" 
	)
	@Schema (
		maxLength = 16,
		description = "precio del producto",
		example = "150" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal precio;
	@Schema (
		maxLength = 100,
		description = "Nombre del producto",
		example = "mi producto" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String producto; 

}