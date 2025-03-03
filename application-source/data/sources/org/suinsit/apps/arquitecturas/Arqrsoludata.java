package org.suinsit.apps.arquitecturas;

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
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.myalm.Almdatabase;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRSOLUDATA" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRSOLUDATA",
	pk = "idxarqrsoludata" 
)
public class Arqrsoludata implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxarqrsoludata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrsoludata;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQSOLUCION0",
		referencedColumnName = "IDXARQSOLUCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqsolucion idarqsolucion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMDATABASE0",
		referencedColumnName = "IDXALMDATABASE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almdatabase idalmdatabase; 

	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	}
	
	public Almdatabase getIdalmdatabase() {
		if(this.idalmdatabase==null)this.idalmdatabase=new org.suinsit.apps.myalm.Almdatabase();
		  return this.idalmdatabase; 
	} 

}