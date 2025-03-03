package com.discovery.suinsit.apps.model.crm;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema (
	name = "Nota" 
)
public class CrmnotaDto { 

	@Schema (
		description = "fecha de alta" 
	)
	private Timestamp alta;
	@Schema (
		description = "nota" 
	)
	private String nota;
	@Schema (
		description = "flag que indica nota privada" 
	)
	private boolean privado; 

}