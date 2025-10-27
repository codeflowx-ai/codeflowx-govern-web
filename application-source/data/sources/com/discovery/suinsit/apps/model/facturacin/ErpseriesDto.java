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
import org.enartframework.nocode.annotacion.ValidEnum;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "facturacin",
	name = "ERPSERIES" 
)
@Schema (
	name = "SERIE" 
)
public class ErpseriesDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean addyear;
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
	private BigDecimal lastvalue;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "",
		example = "solamente admite lo valores: ,000000,00000000,0000000000,#####0,########0,#########0" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",000000,00000000,0000000000,#####0,########0,#########0" 
		},
		message = "solamente admite lo valores: ,000000,00000000,0000000000,#####0,########0,#########0" 
	)
	private String mask;
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
	private String prefix;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean resetyear;
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
	private String sequence; 

}