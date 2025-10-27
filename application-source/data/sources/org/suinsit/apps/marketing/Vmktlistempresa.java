package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
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
	namespace = "marketing",
	type = "VIEW",
	name = "VMKTLISTEMPRESA" 
)
public class Vmktlistempresa implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "claveapi",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String claveapi;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "secret",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String secret;
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
		name = "idxmktmlistnews",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktmlistnews;
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
		label = "{}",
		type = "VARCHAR" 
	)
	private String nombre;
	@Column (
		name = "idcrmempresa0",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "{}",
		type = "LONG" 
	)
	private Long idcrmempresa0;
	@Column (
		name = "idxmktprovmail",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktprovmail;
	private boolean updatable; 

}