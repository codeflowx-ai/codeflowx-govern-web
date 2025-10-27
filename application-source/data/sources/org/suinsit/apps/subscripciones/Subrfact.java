package org.suinsit.apps.subscripciones;

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
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBRFACT" 
)
@Entidad (
	namespace = "subscripciones",
	type = "TABLE",
	name = "SUBRFACT",
	labelMonitor = "",
	pk = "idxsubrfact" 
)
public class Subrfact implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsubrfact",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubrfact;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFACTURA0",
		referencedColumnName = "IDXERPFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfactura iderpfactura;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBSCRIPCION0",
		referencedColumnName = "IDXSUBSCRIPCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subscripcion idsubscripcion; 

	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Subscripcion getIdsubscripcion() {
		if(this.idsubscripcion==null)this.idsubscripcion=new org.suinsit.apps.subscripciones.Subscripcion();
		  return this.idsubscripcion; 
	} 

}