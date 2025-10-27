package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
	namespace = "marketing",
	type = "VIEW",
	name = "VMKTPROPSPROV" 
)
public class Vmktpropsprov implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "mappear",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String mappear;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String nombre;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",string,integer,numeric,booleano,datetime" 
		},
		message = "solamente admite lo valores: ,string,integer,numeric,booleano,datetime" 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "ENUM_STRING" 
	)
	private String tipo;
	@Column (
		name = "idmktprovmail0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idmktprovmail0;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Column (
		name = "idxmktcontactprop",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktcontactprop;
	private boolean updatable; 

}