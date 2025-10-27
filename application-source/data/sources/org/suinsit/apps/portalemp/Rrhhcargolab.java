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
	name = "RRHHCARGOLAB" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHCARGOLAB",
	labelMonitor = "CARGO",
	pk = "idxrrhhcargolab" 
)
public class Rrhhcargolab implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "mandointer",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mandointer;
	@Column (
		name = "directivo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean directivo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cargo;
	@Id
	@Column (
		name = "idxrrhhcargolab",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhcargolab;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhcargolab" 
	)
	private List<Rrhempleado> subrrhempleado; 

	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	} 

}