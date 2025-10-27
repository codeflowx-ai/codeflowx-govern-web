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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.portalemp.Rhfichar;
import org.suinsit.apps.portalemp.Rrhhausencia;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHTURNO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHTURNO",
	pk = "idxrrhhturno" 
)
public class Rrhhturno implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "diasaplica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String diasaplica;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "colorborde",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String colorborde;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "colorfondo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String colorfondo;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Ausencia,Descanso,Trabajo,Vacaciones" 
		},
		message = "solamente admite lo valores: ,Ausencia,Descanso,Trabajo,Vacaciones" 
	)
	@Column (
		name = "tipoturno",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipoturno;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Id
	@Column (
		name = "idxrrhhturno",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhturno;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhturno" 
	)
	private List<Rrhhausencia> subrrhhausencia;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhturno" 
	)
	private List<Rhfichar> subrhfichar; 

	public List<Rrhhausencia> getSubrrhhausencia() {
		if(this.subrrhhausencia==null)this.subrrhhausencia=new ArrayList<>(0);
		  return this.subrrhhausencia; 
	}
	
	public List<Rhfichar> getSubrhfichar() {
		if(this.subrhfichar==null)this.subrhfichar=new ArrayList<>(0);
		  return this.subrhfichar; 
	} 

}