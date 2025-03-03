package com.discovery.suinsit.apps.model.subvenciones;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "subvenciones",
	name = "SUBSUBVENCION" 
)
@Schema (
	name = "Subvencion" 
)
public class SubsubvencionDto { 

}