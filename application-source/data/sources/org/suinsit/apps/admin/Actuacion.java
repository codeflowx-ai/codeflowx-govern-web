package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Entity
@Table (
	name = "MACTUACION" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MACTUACION",
	pk = "idxactuacion" 
)
public class Actuacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "actuacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String actuacion;
	@Id
	@Column (
		name = "idxactuacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxactuacion;
	private boolean updatable; 

}