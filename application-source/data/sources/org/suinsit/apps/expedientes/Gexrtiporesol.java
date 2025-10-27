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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.expedientes.Gexmepediente;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXRTIPORESOL" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXRTIPORESOL",
	labelMonitor = "",
	pk = "idxgexrtiporesol" 
)
public class Gexrtiporesol implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tiporesolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tiporesolucion;
	@Id
	@Column (
		name = "idxgexrtiporesol",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexrtiporesol;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexrtiporesol" 
	)
	private List<Gexmepediente> subgexmepediente; 

	public List<Gexmepediente> getSubgexmepediente() {
		if(this.subgexmepediente==null)this.subgexmepediente=new ArrayList<>(0);
		  return this.subgexmepediente; 
	} 

}