package org.suinsit.apps.pmo;

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
import org.suinsit.apps.pmo.Pmoaprobacion;
import org.suinsit.apps.pmo.Pmodepartament;
import org.suinsit.apps.pmo.Pmoestado;
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmoprioridad;
import org.suinsit.apps.pmo.Pmotipoproyect;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOTIPOPROYECT" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOTIPOPROYECT",
	pk = "idxpmotipoproyect" 
)
public class Pmotipoproyect implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxpmotipoproyect",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmotipoproyect;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String couseralta;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String tmalta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOAPROBACION0",
		referencedColumnName = "IDXPMOAPROBACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoaprobacion idpmoaprobacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMODEPARTAMENT0",
		referencedColumnName = "IDXPMODEPARTAMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmodepartament idpmodepartament;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOESTADO0",
		referencedColumnName = "IDXPMOESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoestado idpmoestado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOPRIORIDAD0",
		referencedColumnName = "IDXPMOPRIORIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoprioridad idpmoprioridad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOTIPOPROYECT0",
		referencedColumnName = "IDXPMOTIPOPROYECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmotipoproyect idpmotipoproyect;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmotipoproyect" 
	)
	private List<Pmomproject> subpmomproject; 

	public Pmoaprobacion getIdpmoaprobacion() {
		if(this.idpmoaprobacion==null)this.idpmoaprobacion=new org.suinsit.apps.pmo.Pmoaprobacion();
		  return this.idpmoaprobacion; 
	}
	
	public Pmodepartament getIdpmodepartament() {
		if(this.idpmodepartament==null)this.idpmodepartament=new org.suinsit.apps.pmo.Pmodepartament();
		  return this.idpmodepartament; 
	}
	
	public Pmoestado getIdpmoestado() {
		if(this.idpmoestado==null)this.idpmoestado=new org.suinsit.apps.pmo.Pmoestado();
		  return this.idpmoestado; 
	}
	
	public Pmoprioridad getIdpmoprioridad() {
		if(this.idpmoprioridad==null)this.idpmoprioridad=new org.suinsit.apps.pmo.Pmoprioridad();
		  return this.idpmoprioridad; 
	}
	
	public Pmotipoproyect getIdpmotipoproyect() {
		if(this.idpmotipoproyect==null)this.idpmotipoproyect=new org.suinsit.apps.pmo.Pmotipoproyect();
		  return this.idpmotipoproyect; 
	}
	
	public List<Pmomproject> getSubpmomproject() {
		if(this.subpmomproject==null)this.subpmomproject=new ArrayList<>(0);
		  return this.subpmomproject; 
	} 

}