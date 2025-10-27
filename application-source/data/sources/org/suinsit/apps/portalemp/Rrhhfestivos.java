package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.portalemp.Rrhhcalendario;
import org.suinsit.apps.portalemp.Rrhhcalenfest;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHFESTIVOS" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHFESTIVOS",
	labelMonitor = "",
	pk = "idxrrhhfestivos" 
)
public class Rrhhfestivos implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcin",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcin;
	@NotNull
	@NotBlank
	@Column (
		name = "fecha",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Id
	@Column (
		name = "idxrrhhfestivos",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhfestivos;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Festivo,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
		},
		message = "solamente admite lo valores: ,Festivo,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHCALENDARIO0",
		referencedColumnName = "IDXRRHHCALENDARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhcalendario idrrhhcalendario;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhhfestivos" 
	)
	private List<Rrhhcalenfest> subrrhhcalenfest; 

	public Rrhhcalendario getIdrrhhcalendario() {
		if(this.idrrhhcalendario==null)this.idrrhhcalendario=new org.suinsit.apps.portalemp.Rrhhcalendario();
		  return this.idrrhhcalendario; 
	}
	
	public List<Rrhhcalenfest> getSubrrhhcalenfest() {
		if(this.subrrhhcalenfest==null)this.subrrhhcalenfest=new ArrayList<>(0);
		  return this.subrrhhcalenfest; 
	} 

}