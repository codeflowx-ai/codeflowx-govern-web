package org.suinsit.apps.portalemp;

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
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.portalemp.Rrhhmtipoestudio;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHMACADEMIC" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHMACADEMIC",
	pk = "idxrrhhmacademic" 
)
public class Rrhhmacademic implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxrrhhmacademic",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhmacademic;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nivelacademico",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nivelacademico;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHMTIPOESTUDIO0",
		referencedColumnName = "IDXRRHHMTIPOESTUDIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhmtipoestudio idrrhhmtipoestudio;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhmacademic" 
	)
	private List<Rrhempleado> subrrhempleado; 

	public Rrhhmtipoestudio getIdrrhhmtipoestudio() {
		if(this.idrrhhmtipoestudio==null)this.idrrhhmtipoestudio=new org.suinsit.apps.portalemp.Rrhhmtipoestudio();
		  return this.idrrhhmtipoestudio; 
	}
	
	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	} 

}