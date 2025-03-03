package org.suinsit.apps.portalemp;

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
import org.suinsit.apps.portalemp.Rrhhmacademic;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHMTIPOESTUDIO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHMTIPOESTUDIO",
	pk = "idxrrhhmtipoestudio" 
)
public class Rrhhmtipoestudio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipoestudio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipoestudio;
	@Id
	@Column (
		name = "idxrrhhmtipoestudio",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhmtipoestudio;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhmtipoestudio" 
	)
	private List<Rrhhmacademic> subrrhhmacademic; 

	public List<Rrhhmacademic> getSubrrhhmacademic() {
		if(this.subrrhhmacademic==null)this.subrrhhmacademic=new ArrayList<>(0);
		  return this.subrrhhmacademic; 
	} 

}