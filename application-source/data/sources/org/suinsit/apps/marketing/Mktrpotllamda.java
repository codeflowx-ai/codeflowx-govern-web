package org.suinsit.apps.marketing;

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
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRPOTLLAMDA" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRPOTLLAMDA",
	labelMonitor = "",
	pk = "idxmktrpotllamda" 
)
public class Mktrpotllamda implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmktrpotllamda",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrpotllamda;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPOTENCIALES0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktpotenciales idmktpotenciales;
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

	public Mktpotenciales getIdmktpotenciales() {
		if(this.idmktpotenciales==null)this.idmktpotenciales=new org.suinsit.apps.marketing.Mktpotenciales();
		  return this.idmktpotenciales; 
	}
	
	public Crmllamada getIdcrmllamada() {
		if(this.idcrmllamada==null)this.idcrmllamada=new org.suinsit.apps.crm.Crmllamada();
		  return this.idcrmllamada; 
	} 

}