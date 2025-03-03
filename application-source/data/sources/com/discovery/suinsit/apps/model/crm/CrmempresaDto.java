package com.discovery.suinsit.apps.model.crm;

import com.discovery.suinsit.apps.model.admin.Mactividad;
import com.discovery.suinsit.apps.model.admin.Mpais;
import com.discovery.suinsit.apps.model.admin.Mprovincia;
import com.discovery.suinsit.apps.model.admin.Mtipoempresa;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.Valid;
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
	namespace = "crm",
	name = "CRMEMPRESA" 
)
@Schema (
	name = "Empresa / Cliente" 
)
public class CrmempresaDto { 

	@Schema (
		description = "fecha de alta del cliente" 
	)
	private LocalDate alta;
	@JsonProperty (
		value = "apellidos" 
	)
	@Schema (
		maxLength = 100,
		example = "mis apellidos" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String apellidos;
	@JsonProperty (
		value = "imagen" 
	)
	@Schema
	private Object avatar;
	@Schema
	private LocalDate baja;
	@JsonProperty (
		value = "identificador" 
	)
	@Schema (
		maxLength = 100,
		example = "B01978474" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String cifnif;
	@JsonProperty (
		value = "ciudad" 
	)
	@Schema (
		maxLength = 100,
		description = "ciudad del cliente",
		example = "mi ciudad" 
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
		description = "codigo interno del cliente",
		example = "3e6442b4-83dc-4299-b3ec-dd37eae70bdf" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String codeuuid;
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
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String correo;
	@JsonProperty (
		value = "direccion" 
	)
	@Schema (
		maxLength = 100,
		example = "mi dicreccion" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String direccion;
	@Schema (
		description = "Num. empleados" 
	)
	private Integer emplados;
	@JsonProperty (
		value = "direccion" 
	)
	@Schema (
		maxLength = 100,
		description = "Nombre de empesa o nombre del cliente si es persona fisica",
		example = "mi dicreccion" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String empresa;
	@JsonProperty (
		value = "provincia" 
	)
	@Schema (
		maxLength = 100,
		description = "Provincia" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String estado;
	@JsonProperty (
		value = "nocampaing" 
	)
	@Schema (
		description = "excluir de campañas de emails",
		example = "" 
	)
	private boolean excludecampaing;
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
	private String idmaillist;
	@JsonProperty (
		value = "industria" 
	)
	@Schema (
		maxLength = 100,
		example = "mi industria" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String industria;
	@JsonProperty (
		value = "" 
	)
	@Schema (
		maxLength = 150,
		description = "Origen del lead",
		example = "solamente admite lo valores: ,WEB,RED SOCIAL,IMPORTACION,REFERENCIA,OTRO" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",WEB,RED SOCIAL,IMPORTACION,REFERENCIA,OTRO" 
		},
		message = "solamente admite lo valores: ,WEB,RED SOCIAL,IMPORTACION,REFERENCIA,OTRO" 
	)
	private String origen;
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
	private BigDecimal recargoeq;
	@JsonProperty (
		value = "refexterna" 
	)
	@Schema (
		maxLength = 100,
		description = "identificador que hace referencia a su aplicación interna",
		example = "123456789" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String refidexterna;
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
		value = "sendemails" 
	)
	@Schema (
		description = "Permite enviar emails",
		example = "true o false" 
	)
	private boolean sendemails;
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
		value = "telefono" 
	)
	@Schema (
		maxLength = 100,
		description = "Numeros de telefono",
		example = "telefono" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String telefono;
	@Schema (
		maxLength = 150 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Fisica,Jurídica" 
		},
		message = "nullsolamente admite lo valores: ,Fisica,Jurídica" 
	)
	private String tipoemp;
	@JsonProperty (
		value = "tipocliente" 
	)
	@Schema (
		maxLength = 150,
		description = "Tipo de cliente",
		example = "solamente admite lo valores: ,Cliente Potencial,Socio,Partner" 
	)
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Cliente Potencial,Socio,Partner" 
		},
		message = "tipoclientesolamente admite lo valores: ,Cliente Potencial,Socio,Partner" 
	)
	private String tipoempre;
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
	@JsonProperty (
		value = "tipoEmpresa" 
	)
	@Valid
	@Schema (
		name = "Tipo Empresa" 
	)
	private Mtipoempresa idmtipoempresa;
	@JsonProperty (
		value = "actividad" 
	)
	@Valid
	@Schema (
		name = "Actividad" 
	)
	private Mactividad idmactividad;
	@JsonProperty (
		value = "province" 
	)
	@Valid
	@Schema (
		name = "province" 
	)
	private Mprovincia idmprovincia;
	@JsonProperty (
		value = "country" 
	)
	@Valid
	@Schema (
		name = "country" 
	)
	private Mpais idmpais; 

}