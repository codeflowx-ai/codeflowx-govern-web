package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.ValidEnum;

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "portalemp",
	type = "VIEW",
	name = "RRHHVDEFAULTCALENDAR" 
)
public class Rrhhvdefaultcalendar implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "festivos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean festivos;
	@Column (
		name = "defecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean defecto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Domingo,Festivo" 
		},
		message = "solamente admite lo valores: ,Domingo,Festivo" 
	)
	@Column (
		name = "domingo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String domingo;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Jueves,Festivo" 
		},
		message = "solamente admite lo valores: ,Jueves,Festivo" 
	)
	@Column (
		name = "jueves",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String jueves;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Lunes,Festivo" 
		},
		message = "solamente admite lo valores: ,Lunes,Festivo" 
	)
	@Column (
		name = "lunes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String lunes;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Martes,Festivo" 
		},
		message = "solamente admite lo valores: ,Martes,Festivo" 
	)
	@Column (
		name = "martes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String martes;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Miercoles,Festivo" 
		},
		message = "solamente admite lo valores: ,Miercoles,Festivo" 
	)
	@Column (
		name = "miercoles",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String miercoles;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Sabado,Festivo" 
		},
		message = "solamente admite lo valores: ,Sabado,Festivo" 
	)
	@Column (
		name = "sabado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String sabado;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Viernes,Festivo" 
		},
		message = "solamente admite lo valores: ,Viernes,Festivo" 
	)
	@Column (
		name = "viernes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String viernes;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Festivo,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
		},
		message = "solamente admite lo valores: ,Festivo,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
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
	@NotNull
	@NotBlank
	@Column (
		name = "fecha",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DATE" 
	)
	private Date fecha;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcin",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String descripcin;
	@Column (
		name = "idxrrhhfestivos",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhfestivos;
	@Column (
		name = "idrrhhcalendario0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idrrhhcalendario0;
	private boolean updatable; 

}