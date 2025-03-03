package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.myalm.Almrversioncomp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMCOMPONENT" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMCOMPONENT",
	labelMonitor = "COMPONENT",
	pk = "idxalmcomponent" 
)
public class Almcomponent implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "application",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean application;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "component",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String component;
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String description;
	@Id
	@Column (
		name = "idxalmcomponent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmcomponent;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "imagen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String imagen;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "order",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal order;
	@Column (
		name = "setup",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean setup;
	@Column (
		name = "sistema",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sistema;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "template",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String template;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmcomponent" 
	)
	private List<Almrversioncomp> subalmrversioncomp; 

	public List<Almrversioncomp> getSubalmrversioncomp() {
		if(this.subalmrversioncomp==null)this.subalmrversioncomp=new ArrayList<>(0);
		  return this.subalmrversioncomp; 
	} 

}