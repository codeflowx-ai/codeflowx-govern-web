package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
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
	namespace = "subvenciones",
	type = "VIEW_CHARTS",
	name = "IMPORTESCATEGORIA" 
)
public class Importescategoria implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "categoria",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String categoria;
	private boolean updatable; 

}