package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.expedientes.Gexrreqexp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXMTIPOREQ" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMTIPOREQ",
	labelMonitor = "TIPOREQUISITO",
	pk = "idxgexmtiporeq" 
)
public class Gexmtiporeq implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "plantillaescrito",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String plantillaescrito;
	@Column (
		name = "adjuntardoc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean adjuntardoc;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tiporequisito",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tiporequisito;
	@Id
	@Column (
		name = "idxgexmtiporeq",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmtiporeq;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmtiporeq" 
	)
	private List<Gexrreqexp> subgexrreqexp; 

	public List<Gexrreqexp> getSubgexrreqexp() {
		if(this.subgexrreqexp==null)this.subgexrreqexp=new ArrayList<>(0);
		  return this.subgexrreqexp; 
	} 

}