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
	name = "RAGAUDIT" 
)
@Schema (
	name = "RAG_AUDIT" 
)
public class ComSuinsitAppsSuinlessRagauditDto { 

}