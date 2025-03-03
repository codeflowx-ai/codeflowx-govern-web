package com.discovery.suinsit.apps.model.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "admin",
	name = "SSORUSEROL" 
)
@Schema
public class SsoruserolDto { 

}