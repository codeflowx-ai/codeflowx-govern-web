package com.discovery.suinsit.apps.model.rag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enartframework.nocode.annotacion.Entidad;

@Data
@NoArgsConstructor
@Entidad (
	type = "TABLE",
	namespace = "rag",
	name = "RAGDATASOURCE" 
)
@Schema (
	name = "RAG_DATASOURCE" 
)
public class ComSuinsitAppsSuinlessRagdatasourceDto { 

}