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
	name = "GEXMESTADO" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMESTADO",
	labelMonitor = "ESTADO",
	pk = "idxgexmestado" 
)
public class Gexmestado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estado;
	@Id
	@Column (
		name = "idxgexmestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmestado;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmestado" 
	)
	private List<Gexmepediente> subgexmepediente; 

	public List<Gexmepediente> getSubgexmepediente() {
		if(this.subgexmepediente==null)this.subgexmepediente=new ArrayList<>(0);
		  return this.subgexmepediente; 
	} 

}