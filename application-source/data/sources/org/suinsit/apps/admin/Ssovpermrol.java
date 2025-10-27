package org.suinsit.apps.admin;

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

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "admin",
	type = "VIEW",
	name = "SSOVPERMROL" 
)
public class Ssovpermrol implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "aplicacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String aplicacion;
	@Column (
		name = "idxaplicacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxaplicacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "icono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String icono;
	@Column (
		name = "idxssomenu",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssomenu;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "menu",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String menu;
	@Column (
		name = "idxssomenuitem",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssomenuitem;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "item",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String item;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String namespace;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "page",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String page;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "url",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String url;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "rol",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String rol;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dashboard",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String dashboard;
	private boolean updatable; 

}