package org.suinsit.apps.subscripciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "TIPOSUBSCRIP" 
)
@Entidad (
	namespace = "subscripciones",
	type = "TABLE",
	name = "TIPOSUBSCRIP",
	labelMonitor = "TIPO",
	pk = "idxtiposubscrip" 
)
public class Tiposubscrip implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	@Id
	@Column (
		name = "idxtiposubscrip",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtiposubscrip;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idtiposubscrip" 
	)
	private List<Subscripcion> subsubscripcion; 

	public List<Subscripcion> getSubsubscripcion() {
		if(this.subsubscripcion==null)this.subsubscripcion=new ArrayList<>(0);
		  return this.subsubscripcion; 
	} 

}