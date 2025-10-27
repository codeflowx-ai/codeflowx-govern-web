package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.portalemp.Rrhhcalendario;
import org.suinsit.apps.portalemp.Rrhhfestivos;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHCALENFEST" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHCALENFEST",
	pk = "idxrrhhcalenfest" 
)
public class Rrhhcalenfest implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxrrhhcalenfest",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhcalenfest;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHFESTIVOS0",
		referencedColumnName = "IDXRRHHFESTIVOS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhfestivos idrrhhfestivos; 

	public Rrhhcalendario getIdrrhhcalendario() {
		if(this.idrrhhcalendario==null)this.idrrhhcalendario=new org.suinsit.apps.portalemp.Rrhhcalendario();
		  return this.idrrhhcalendario; 
	}
	
	public Rrhhfestivos getIdrrhhfestivos() {
		if(this.idrrhhfestivos==null)this.idrrhhfestivos=new org.suinsit.apps.portalemp.Rrhhfestivos();
		  return this.idrrhhfestivos; 
	} 

}