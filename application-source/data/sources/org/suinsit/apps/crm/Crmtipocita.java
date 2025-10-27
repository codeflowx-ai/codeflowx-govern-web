package org.suinsit.apps.crm;

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
import org.suinsit.apps.citaprev.Citmagenda;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMTIPOCITA" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMTIPOCITA",
	pk = "idxcrmtipocita" 
)
public class Crmtipocita implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Id
	@Column (
		name = "idxcrmtipocita",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmtipocita;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipocita",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipocita;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmtipocita" 
	)
	private List<Citmagenda> subcrmagenda; 

	public List<Citmagenda> getSubcrmagenda() {
		if(this.subcrmagenda==null)this.subcrmagenda=new ArrayList<>(0);
		  return this.subcrmagenda; 
	} 

}