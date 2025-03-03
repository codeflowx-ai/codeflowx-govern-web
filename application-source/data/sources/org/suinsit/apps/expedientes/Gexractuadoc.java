package org.suinsit.apps.expedientes;

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
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.expedientes.Gexmactuacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXRACTUADOC" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXRACTUADOC",
	labelMonitor = "",
	pk = "idxgexractuadoc" 
)
public class Gexractuadoc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxgexractuadoc",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexractuadoc;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMACTUACION0",
		referencedColumnName = "IDXGEXMACTUACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmactuacion idgexmactuacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDDOCFICHERO0",
		referencedColumnName = "IDXDOCFICHERO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Docfichero iddocfichero; 

	public Gexmactuacion getIdgexmactuacion() {
		if(this.idgexmactuacion==null)this.idgexmactuacion=new org.suinsit.apps.expedientes.Gexmactuacion();
		  return this.idgexmactuacion; 
	}
	
	public Docfichero getIddocfichero() {
		if(this.iddocfichero==null)this.iddocfichero=new org.suinsit.apps.document.Docfichero();
		  return this.iddocfichero; 
	} 

}