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
import org.suinsit.apps.atlas.Atlcomponent;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRSOLCOMP" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRSOLCOMP",
	pk = "idxarqrsolcomp" 
)
public class Arqrsolcomp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxarqrsolcomp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrsolcomp;
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
		name = "IDATLCOMPONENT0",
		referencedColumnName = "IDXATLCOMPONENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlcomponent idatlcomponent; 

	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	}
	
	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	} 

}