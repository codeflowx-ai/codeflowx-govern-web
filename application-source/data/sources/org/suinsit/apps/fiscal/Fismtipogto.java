package org.suinsit.apps.fiscal;

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
import org.suinsit.apps.fiscal.Fismgastos;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FISMTIPOGTO" 
)
@Entidad (
	namespace = "fiscal",
	type = "TABLE",
	name = "FISMTIPOGTO",
	labelMonitor = "TIPOGASTO",
	pk = "idxfismtipogto" 
)
public class Fismtipogto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ctactble",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ctactble;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipogasto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipogasto;
	@Id
	@Column (
		name = "idxfismtipogto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfismtipogto;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfismtipogto" 
	)
	private List<Fismgastos> subfismgastos; 

	public List<Fismgastos> getSubfismgastos() {
		if(this.subfismgastos==null)this.subfismgastos=new ArrayList<>(0);
		  return this.subfismgastos; 
	} 

}