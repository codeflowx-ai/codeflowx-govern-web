package org.suinsit.apps.suinless;

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
import org.suinsit.apps.suinless.Slesmodel;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLMLICENCE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLMLICENCE",
	labelMonitor = "LICENCE",
	pk = "idxslmlicence" 
)
public class Slmlicence implements Serializable { 

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
		name = "personal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean personal;
	@Column (
		name = "opensource",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean opensource;
	@Column (
		name = "commercial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean commercial;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "licence",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String licence;
	@Id
	@Column (
		name = "idxslmlicence",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslmlicence;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmlicence" 
	)
	private List<Slesmodel> subslesmodel; 

	public List<Slesmodel> getSubslesmodel() {
		if(this.subslesmodel==null)this.subslesmodel=new ArrayList<>(0);
		  return this.subslesmodel; 
	} 

}