package com.discovery.suinsit.apps.model.crm;

import com.discovery.suinsit.apps.model.admin.Mcargo;
import com.discovery.suinsit.apps.model.admin.Morigen;
import com.discovery.suinsit.apps.model.admin.Mpais;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Object;
import java.lang.String;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.FieldType;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "crm",
	name = "CRMCONTACTO" 
)
@Schema (
	name = "Contacto" 
)
public class CrmcontactoDto { 

	@JsonProperty (
		value = "apellidos" 
	)
	@Schema (
		maxLength = 100,
		description = "apellidos del contacto",
		example = "guzman el bueno" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String apellidos;
	@JsonProperty (
		value = "imagen" 
	)
	@Schema (
		description = "imagen, avatar del contacto" 
	)
	private Object avatar;
	@JsonProperty (
		value = "ciudad" 
	)
	@Schema (
		maxLength = 100,
		description = "Ciudad del contacto",
		example = "madrid" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String ciudad;
	@JsonProperty (
		value = "uuid" 
	)
	@Schema (
		maxLength = 100,
		description = "Código del contacto",
		example = "3e6442b4-83dc-4299-b3ec-dd37eae70bdf" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String codigouuid;
	@JsonProperty (
		value = "email" 
	)
	@Schema (
		maxLength = 100,
		description = "correo electronico",
		example = "micorreo@midominio.es" 
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
	private String correo;
	@JsonProperty (
		value = "departamento" 
	)
	@Schema (
		maxLength = 100,
		description = "Departamento al que pertenece",
		example = "compras" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String departamento;
	@JsonProperty (
		value = "dirección" 
	)
	@Schema (
		maxLength = 100,
		description = "dirección del contacto",
		example = "mi dirección" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String direccion;
	@JsonProperty (
		value = "provincia" 
	)
	@Schema (
		maxLength = 100,
		description = "Provincia/Estado",
		example = "cádiz" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String estado;
	@JsonProperty (
		value = "excludeCampaings" 
	)
	@Schema (
		description = "Excluir de las campañas de email",
		example = "true/false" 
	)
	private boolean excludecampaing;
	@JsonProperty (
		value = "fecnacimiento" 
	)
	@Schema (
		description = "Fecha de nacimiento" 
	)
	private LocalDate fecnacimiento;
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
	private String idmaillist;
	@JsonProperty (
		value = "movil" 
	)
	@Schema (
		maxLength = 100,
		description = "Teléfono móvil",
		example = "+34123456" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String movil;
	@JsonProperty (
		value = "nombre" 
	)
	@Schema (
		maxLength = 100,
		description = "Nombre del contacto",
		example = "Manuel" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String nombre;
	@JsonProperty (
		value = "pais" 
	)
	@Schema (
		maxLength = 100,
		description = "País",
		example = "españa" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String pais;
	@JsonProperty (
		value = "telefono" 
	)
	@Schema (
		maxLength = 100,
		description = "teléfono",
		example = "914 456 741" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String telefono;
	@JsonProperty (
		value = "origen" 
	)
	@Valid
	@Schema (
		name = "origen" 
	)
	private Morigen idmorigen;
	@JsonProperty (
		value = "cargo" 
	)
	@Valid
	@Schema (
		name = "cargo" 
	)
	private Mcargo idmcargo;
	@JsonProperty (
		value = "pais" 
	)
	@Valid
	@Schema (
		name = "pais" 
	)
	private Mpais idmpais; 

}