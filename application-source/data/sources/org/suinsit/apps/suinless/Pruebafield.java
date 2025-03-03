package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Byte;
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
	name = "PRUEBAFIELD" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "PRUEBAFIELD",
	pk = "idxpruebafield" 
)
public class Pruebafield implements Serializable { 

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
		name = "mapobjetos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "MAPOBJECT" 
	)
	private Map mapobjetos;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "listafields",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LIST_STRING" 
	)
	private List listafields;
	@Id
	@Column (
		name = "idxpruebafield",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpruebafield;
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
	private Byte[] vectorfield;
	private boolean updatable; 

}