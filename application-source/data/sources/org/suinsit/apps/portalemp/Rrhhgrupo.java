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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.portalemp.Rrhhcalendario;
import org.suinsit.apps.portalemp.Rrhhrempgrupo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHGRUPO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHGRUPO",
	pk = "idxrrhhgrupo" 
)
public class Rrhhgrupo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "color",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String color;
	@Id
	@Column (
		name = "idxrrhhgrupo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhgrupo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Nombre",
		type = "VARCHAR" 
	)
	private String nombre;
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
		mappedBy = "idrrhhgrupo" 
	)
	private List<Rrhhrempgrupo> subrrhhrempgrupo; 

	public Rrhhcalendario getIdrrhhcalendario() {
		if(this.idrrhhcalendario==null)this.idrrhhcalendario=new org.suinsit.apps.portalemp.Rrhhcalendario();
		  return this.idrrhhcalendario; 
	}
	
	public List<Rrhhrempgrupo> getSubrrhhrempgrupo() {
		if(this.subrrhhrempgrupo==null)this.subrrhhrempgrupo=new ArrayList<>(0);
		  return this.subrrhhrempgrupo; 
	} 

}