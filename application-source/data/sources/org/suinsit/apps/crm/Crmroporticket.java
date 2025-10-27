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
import org.suinsit.apps.asesor4.Sacticket;
import org.suinsit.apps.crm.Crmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMROPORTICKET" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMROPORTICKET",
	pk = "idxcrmroporticket" 
)
public class Crmroporticket implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmroporticket",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmroporticket;
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
		name = "IDSACTICKET0",
		referencedColumnName = "IDXTICKET",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sacticket idsacticket; 

	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Sacticket getIdsacticket() {
		if(this.idsacticket==null)this.idsacticket=new org.suinsit.apps.asesor4.Sacticket();
		  return this.idsacticket; 
	} 

}