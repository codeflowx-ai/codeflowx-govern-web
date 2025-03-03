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
import org.suinsit.apps.arquitecturas.Arqmarchitecture;
import org.suinsit.apps.myalm.Almdatabase;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRARQBBDD" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRARQBBDD",
	pk = "idxarqrarqbbdd" 
)
public class Arqrarqbbdd implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxarqrarqbbdd",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrarqbbdd;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQMARCHITECTURE0",
		referencedColumnName = "IDXARQMARCHITECTURE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmarchitecture idarqmarchitecture;
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

	public Arqmarchitecture getIdarqmarchitecture() {
		if(this.idarqmarchitecture==null)this.idarqmarchitecture=new org.suinsit.apps.arquitecturas.Arqmarchitecture();
		  return this.idarqmarchitecture; 
	}
	
	public Almdatabase getIdalmdatabase() {
		if(this.idalmdatabase==null)this.idalmdatabase=new org.suinsit.apps.myalm.Almdatabase();
		  return this.idalmdatabase; 
	} 

}