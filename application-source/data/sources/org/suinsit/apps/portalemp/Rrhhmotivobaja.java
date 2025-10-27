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
import org.suinsit.apps.portalemp.Rrhempleado;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHMOTIVOBAJA" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHMOTIVOBAJA",
	labelMonitor = "MOTIVOBAJA",
	pk = "idxrrhhmotivobaja" 
)
public class Rrhhmotivobaja implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "motivobaja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String motivobaja;
	@Id
	@Column (
		name = "idxrrhhmotivobaja",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhmotivobaja;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhmotivobaja" 
	)
	private List<Rrhempleado> subrrhempleado; 

	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	} 

}