package com.discovery.suinsit.apps.model.suinless;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "suinless",
	name = "SLRPROMPSECURE" 
)
@Schema
public class SlrprompsecureDto { 

}