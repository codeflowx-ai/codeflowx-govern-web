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
import org.suinsit.apps.arquitecturas.Arqaplicacion;
import org.suinsit.apps.arquitecturas.Arqsolucion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRSOLAPP" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRSOLAPP",
	labelMonitor = "",
	pk = "idxarqrsolapp" 
)
public class Arqrsolapp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxarqrsolapp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrsolapp;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQAPLICACION0",
		referencedColumnName = "IDXARQAPLICACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqaplicacion idarqaplicacion;
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

	public Arqaplicacion getIdarqaplicacion() {
		if(this.idarqaplicacion==null)this.idarqaplicacion=new org.suinsit.apps.arquitecturas.Arqaplicacion();
		  return this.idarqaplicacion; 
	}
	
	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	} 

}