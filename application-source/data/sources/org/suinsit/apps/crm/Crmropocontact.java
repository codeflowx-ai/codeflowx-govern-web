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
import org.suinsit.apps.admin.Mrolcargo;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMROPOCONTACT" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMROPOCONTACT",
	pk = "idxcrmropocontact" 
)
public class Crmropocontact implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmropocontact",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmropocontact;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMCONTACTO0",
		referencedColumnName = "IDXCRMCONTACTO",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmcontacto idcrmcontacto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMOPORTUNIDAD0",
		referencedColumnName = "IDXOPORTUNIDAD",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmoportunidad idcrmoportunidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMROLCARGO0",
		referencedColumnName = "IDXMROLCARGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrolcargo idmrolcargo; 

	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Mrolcargo getIdmrolcargo() {
		if(this.idmrolcargo==null)this.idmrolcargo=new org.suinsit.apps.admin.Mrolcargo();
		  return this.idmrolcargo; 
	} 

}