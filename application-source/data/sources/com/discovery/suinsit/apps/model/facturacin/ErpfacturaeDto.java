package com.discovery.suinsit.apps.model.facturacin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Object;
import java.lang.String;
import java.sql.Timestamp;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "facturacin",
	name = "ERPFACTURAE" 
)
@Schema (
	name = "FacturaE" 
)
public class ErpfacturaeDto { 

	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 500,
		description = "",
		example = "" 
	)
	@Size (
		min = 0,
		max = 500 
	)
	private String aditionalinformation;
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
	private String bic;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private LocalDate enddate;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Timestamp fecsigned;
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
	private String iban;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private boolean signed;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private LocalDate startdate;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		description = "",
		example = "" 
	)
	private Object xmlsigned; 

}