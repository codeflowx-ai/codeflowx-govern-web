package org.suinsit.apps.arquitecturas;

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
import org.suinsit.apps.arquitecturas.Arqmpromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQTIPOPROMPT" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQTIPOPROMPT",
	labelMonitor = "TIPO",
	pk = "idxarqtipoprompt" 
)
public class Arqtipoprompt implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "asistente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean asistente;
	@Column (
		name = "deployment",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean deployment;
	@Column (
		name = "generation",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean generation;
	@Id
	@Column (
		name = "idxarqtipoprompt",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqtipoprompt;
	@Column (
		name = "requisitos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean requisitos;
	@Column (
		name = "rules",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean rules;
	@Column (
		name = "source",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean source;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqtipoprompt" 
	)
	private List<Arqmpromp> subarqmpromp; 

	public List<Arqmpromp> getSubarqmpromp() {
		if(this.subarqmpromp==null)this.subarqmpromp=new ArrayList<>(0);
		  return this.subarqmpromp; 
	} 

}