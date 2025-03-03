package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
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
import org.enartframework.nocode.annotacion.ValidEnum;

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "portalemp",
	type = "VIEW",
	name = "VRRHCOUNTFICHAJE" 
)
public class Vrrhcountfichaje implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "total",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal total;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Trabajo,Ausencia,Descanso,Otro" 
		},
		message = "solamente admite lo valores: ,Trabajo,Ausencia,Descanso,Otro" 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String tipo;
	@Column (
		name = "anio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer anio;
	@Column (
		name = "mes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer mes;
	@Column (
		name = "aprobada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean aprobada;
	@Column (
		name = "idrrhempleado0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idrrhempleado0;
	private boolean updatable; 

}