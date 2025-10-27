package org.suinsit.apps.bpmn;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Mmodulo;
import org.suinsit.apps.bpmn.Bpmmproces;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "BPMCATEGORIA" 
)
@Entidad (
	namespace = "bpmn",
	type = "TABLE",
	name = "BPMCATEGORIA",
	labelMonitor = "",
	pk = "idxbpmcategoria" 
)
public class Bpmcategoria implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "categoria",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String categoria;
	@Id
	@Column (
		name = "idxbpmcategoria",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxbpmcategoria;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMMODULO0",
		referencedColumnName = "IDXMMODULO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mmodulo idmmodulo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idbpmcategoria" 
	)
	private List<Bpmmproces> subbpmmproces; 

	public Mmodulo getIdmmodulo() {
		if(this.idmmodulo==null)this.idmmodulo=new org.suinsit.apps.admin.Mmodulo();
		  return this.idmmodulo; 
	}
	
	public List<Bpmmproces> getSubbpmmproces() {
		if(this.subbpmmproces==null)this.subbpmmproces=new ArrayList<>(0);
		  return this.subbpmmproces; 
	} 

}