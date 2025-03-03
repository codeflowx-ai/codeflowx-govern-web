package com.discovery.suinsit.apps.model.crm;

import com.discovery.suinsit.apps.model.admin.Mtipooportunidad;
import com.discovery.suinsit.apps.model.admin.Mtipoprioridad;
import com.discovery.suinsit.apps.model.crm.Crmempresa;
import com.discovery.suinsit.apps.model.crm.Crmetapa;
import com.discovery.suinsit.apps.model.crm.Crmpipeline;
import com.discovery.suinsit.apps.model.crm.Crmtiponegocio;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "crm",
	name = "CRMOPORTUNIDAD" 
)
@Schema (
	name = "Oportunidad" 
)
public class CrmoportunidadDto { 

	@Schema (
		description = "fecha de alta",
		example = "01/01/2022" 
	)
	@NotNull
	@NotBlank
	private LocalDate alta;
	@JsonProperty (
		value = "archivada" 
	)
	@Schema (
		description = "oportunidad acrhivada",
		example = "true/false" 
	)
	private boolean archived;
	@JsonProperty (
		value = "cerrada" 
	)
	@Schema (
		description = "Oportunidad Cerrada",
		example = "true/false" 
	)
	private boolean cerrado;
	@JsonProperty (
		value = "uuid" 
	)
	@Schema (
		maxLength = 100,
		description = "Codigo de oportunidad",
		example = "3e6442b4-83dc-4299-b3ec-dd37eae70bdf" 
	)
	@Size (
		min = 0,
		max = 100 
	)
	private String codigouuid;
	@JsonProperty (
		value = "descripcion" 
	)
	@Schema (
		description = "descripción oportunidad",
		example = "mi oportunidad" 
	)
	private String descripcion;
	@JsonProperty (
		value = "cierre" 
	)
	@Schema (
		description = "fecha de cierre",
		example = "01/01/2023" 
	)
	private LocalDate feccierre;
	@JsonProperty (
		value = "cierreEstimado" 
	)
	@Schema (
		description = "Fecha de cierre estimado",
		example = "01/01/2023" 
	)
	private LocalDate feccierreestima;
	@JsonProperty (
		value = "importeFinal" 
	)
	@Schema (
		maxLength = 16,
		description = "Importe cerrado",
		example = "100.000" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal importefinal;
	@JsonProperty (
		value = "importePrev" 
	)
	@Schema (
		maxLength = 16,
		description = "Importe previsto",
		example = "300.000" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal importeprev;
	@JsonProperty (
		value = "motivoCierre" 
	)
	@Schema (
		description = "Motivo de cierre",
		example = "oferta no aceptada" 
	)
	private String motivocierre;
	@JsonProperty (
		value = "oportunidad" 
	)
	@Schema (
		maxLength = 100,
		description = "Nombre de la oportunidad",
		example = "Mi Oportunidad" 
	)
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	private String oportunidad;
	@JsonProperty (
		value = "Probabilidad" 
	)
	@Schema (
		maxLength = 16,
		description = "Probabilidad de éxito",
		example = "15" 
	)
	@Size (
		min = 0,
		max = 16 
	)
	private BigDecimal probabilidad;
	@Schema (
		description = "Ultimo contacto",
		example = "01/01/2023" 
	)
	private LocalDate ultimocontacto;
	@JsonProperty (
		value = "tipoOportunidad" 
	)
	@Valid
	@Schema (
		name = "tipoOportunidad" 
	)
	private Mtipooportunidad idmtipooportunidad;
	@JsonProperty (
		value = "tipoPrioridad" 
	)
	@Valid
	@Schema (
		name = "TipoPrioridad" 
	)
	private Mtipoprioridad idmtipoprioridad;
	@JsonProperty (
		value = "etapa" 
	)
	@Valid
	@Schema (
		name = "etapa" 
	)
	private Crmetapa idcrmetapa;
	@JsonProperty (
		value = "pipeline" 
	)
	@Valid
	@Schema (
		name = "pipeline" 
	)
	private Crmpipeline idcrmpipeline;
	@JsonProperty (
		value = "tipoNegocio" 
	)
	@Valid
	@Schema (
		name = "TipoNegocio" 
	)
	private Crmtiponegocio idcrmtiponegocio;
	@JsonProperty (
		value = "cliente" 
	)
	@Valid
	@Schema (
		name = "cliente" 
	)
	private Crmempresa idcrmempresa; 

}