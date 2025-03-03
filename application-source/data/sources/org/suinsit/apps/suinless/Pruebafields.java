package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
	name = "PRUEBAFIELDS" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "PRUEBAFIELDS",
	pk = "idx" 
)
public class Pruebafields implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "mapstring",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "MAPSTRING" 
	)
	private Map mapstring;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "mapobject",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "MAPOBJECT" 
	)
	private Map mapobject;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "listastring",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LIST_STRING" 
	)
	private List listastring;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "vectorfield",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VECTOR" 
	)
	private float[] vectorfield;
	@Id
	@Column (
		name = "idx",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idx;
	private boolean updatable; 

}