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
import org.suinsit.apps.portalemp.Rrhhcalenfest;
import org.suinsit.apps.portalemp.Rrhhfestivos;
import org.suinsit.apps.portalemp.Rrhhgrupo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHCALENDARIO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHCALENDARIO",
	labelMonitor = "",
	pk = "idxrrhhcalendario" 
)
public class Rrhhcalendario implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "festivos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean festivos;
	@Column (
		name = "defecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean defecto;
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
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Domingo,Festivo" 
		},
		message = "solamente admite lo valores: ,Domingo,Festivo" 
	)
	@Column (
		name = "domingo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String domingo;
	@Id
	@Column (
		name = "idxrrhhcalendario",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhcalendario;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Jueves,Festivo" 
		},
		message = "solamente admite lo valores: ,Jueves,Festivo" 
	)
	@Column (
		name = "jueves",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String jueves;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Lunes,Festivo" 
		},
		message = "solamente admite lo valores: ,Lunes,Festivo" 
	)
	@Column (
		name = "lunes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String lunes;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Martes,Festivo" 
		},
		message = "solamente admite lo valores: ,Martes,Festivo" 
	)
	@Column (
		name = "martes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String martes;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Miercoles,Festivo" 
		},
		message = "solamente admite lo valores: ,Miercoles,Festivo" 
	)
	@Column (
		name = "miercoles",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String miercoles;
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
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Sabado,Festivo" 
		},
		message = "solamente admite lo valores: ,Sabado,Festivo" 
	)
	@Column (
		name = "sabado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String sabado;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Viernes,Festivo" 
		},
		message = "solamente admite lo valores: ,Viernes,Festivo" 
	)
	@Column (
		name = "viernes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String viernes;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhcalendario" 
	)
	private List<Rrhhcalenfest> subrrhhcalenfest;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhcalendario" 
	)
	private List<Rrhhgrupo> subrrhhgrupo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhcalendario" 
	)
	private List<Rrhhfestivos> subrrhhfestivos; 

	public List<Rrhhcalenfest> getSubrrhhcalenfest() {
		if(this.subrrhhcalenfest==null)this.subrrhhcalenfest=new ArrayList<>(0);
		  return this.subrrhhcalenfest; 
	}
	
	public List<Rrhhgrupo> getSubrrhhgrupo() {
		if(this.subrrhhgrupo==null)this.subrrhhgrupo=new ArrayList<>(0);
		  return this.subrrhhgrupo; 
	}
	
	public List<Rrhhfestivos> getSubrrhhfestivos() {
		if(this.subrrhhfestivos==null)this.subrrhhfestivos=new ArrayList<>(0);
		  return this.subrrhhfestivos; 
	} 

}