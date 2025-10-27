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
import org.suinsit.apps.arquitecturas.Arqmdeployments;
import org.suinsit.apps.arquitecturas.Arqsolucion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRSOLDEPLOY" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRSOLDEPLOY",
	labelMonitor = "",
	pk = "idxarqrsoldeploy" 
)
public class Arqrsoldeploy implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxarqrsoldeploy",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrsoldeploy;
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
		name = "IDARQMDEPLOYMENTS0",
		referencedColumnName = "IDXARQMDEPLOYMENTS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmdeployments idarqmdeployments; 

	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	}
	
	public Arqmdeployments getIdarqmdeployments() {
		if(this.idarqmdeployments==null)this.idarqmdeployments=new org.suinsit.apps.arquitecturas.Arqmdeployments();
		  return this.idarqmdeployments; 
	} 

}