package com.discovery.suinsit.apps.model.arquitecturas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "arquitecturas",
	name = "ARQRSOLAPP" 
)
@Schema (
	name = "" 
)
public class ArqrsolappDto { 

}