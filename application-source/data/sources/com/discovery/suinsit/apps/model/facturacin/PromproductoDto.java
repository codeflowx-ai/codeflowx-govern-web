package com.discovery.suinsit.apps.model.facturacin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "facturacin",
	name = "PROMPRODUCTO" 
)
@Schema (
	name = "Producto" 
)
public class PromproductoDto { 

}