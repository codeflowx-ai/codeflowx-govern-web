package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.pmo.Pmomactividad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOPROCESO" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOPROCESO",
	labelMonitor = "",
	pk = "idxpmoproceso" 
)
public class Pmoproceso implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Id
	@Column (
		name = "idxpmoproceso",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoproceso;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proceso",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proceso;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMACTIVIDAD0",
		referencedColumnName = "IDXPMOMACTIVIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomactividad idpmomactividad; 

	public Pmomactividad getIdpmomactividad() {
		if(this.idpmomactividad==null)this.idpmomactividad=new org.suinsit.apps.pmo.Pmomactividad();
		  return this.idpmomactividad; 
	} 

}