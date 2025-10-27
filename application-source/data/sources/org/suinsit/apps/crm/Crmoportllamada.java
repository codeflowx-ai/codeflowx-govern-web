package org.suinsit.apps.crm;

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
import org.suinsit.apps.crm.Crmllamada;
import org.suinsit.apps.crm.Crmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMOPORTLLAMADA" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMOPORTLLAMADA",
	pk = "idxcrmoportllamada" 
)
public class Crmoportllamada implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmoportllamada",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmoportllamada;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMOPORTUNIDAD0",
		referencedColumnName = "IDXOPORTUNIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmoportunidad idcrmoportunidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMLLAMADA0",
		referencedColumnName = "IDXCRMLLAMADA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmllamada idcrmllamada; 

	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmllamada getIdcrmllamada() {
		if(this.idcrmllamada==null)this.idcrmllamada=new org.suinsit.apps.crm.Crmllamada();
		  return this.idcrmllamada; 
	} 

}