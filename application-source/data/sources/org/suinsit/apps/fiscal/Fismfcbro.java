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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.fiscal.Fismfactura;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FISMFCBRO" 
)
@Entidad (
	namespace = "fiscal",
	type = "TABLE",
	name = "FISMFCBRO",
	labelMonitor = "FORMACBRO",
	pk = "idxfismfcbro" 
)
public class Fismfcbro implements Serializable { 

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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "formacbro",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String formacbro;
	@Id
	@Column (
		name = "idxfismfcbro",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfismfcbro;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfismfcbro" 
	)
	private List<Fismfactura> subfismfactura; 

	public List<Fismfactura> getSubfismfactura() {
		if(this.subfismfactura==null)this.subfismfactura=new ArrayList<>(0);
		  return this.subfismfactura; 
	} 

}