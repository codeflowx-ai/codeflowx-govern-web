package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "crm",
	type = "VIEW_CHARTS",
	name = "VCOUNTOPORTFROMYEAR" 
)
public class Vcountoportfromyear implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "etapa",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String etapa;
	@NotNull
	@NotBlank
	@Column (
		name = "year",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "DATE" 
	)
	private Date year;
	@NotNull
	@NotBlank
	@Column (
		name = "month",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "DATE" 
	)
	private Date month;
	@Column (
		name = "idxoportunidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxoportunidad;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importefinal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "DECIMAL" 
	)
	private BigDecimal importefinal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importeprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "DECIMAL" 
	)
	private BigDecimal importeprev;
	private boolean updatable; 

}