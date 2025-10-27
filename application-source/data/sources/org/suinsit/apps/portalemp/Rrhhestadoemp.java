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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	name = "RRHHESTADOEMP" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHESTADOEMP",
	labelMonitor = "",
	pk = "idxrrhhestadoemp" 
)
public class Rrhhestadoemp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxrrhhestadoemp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhestadoemp;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Estado",
		type = "VARCHAR" 
	)
	private String estado;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhestadoemp" 
	)
	private List<Rrhempleado> subrrhempleado; 

	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	} 

}