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
import org.suinsit.apps.crm.Crmpotenciales;
import org.suinsit.apps.marketing.Mktmlistnews;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMRPOTLISTA" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMRPOTLISTA",
	labelMonitor = "",
	pk = "idxcrmrpotlista" 
)
public class Crmrpotlista implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmrpotlista",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmrpotlista;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMPOTENCIALES0",
		referencedColumnName = "IDXCRMPOTENCIALES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmpotenciales idcrmpotenciales;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTMLISTNEWS0",
		referencedColumnName = "IDXMKTMLISTNEWS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmlistnews idmktmlistnews; 

	public Crmpotenciales getIdcrmpotenciales() {
		if(this.idcrmpotenciales==null)this.idcrmpotenciales=new org.suinsit.apps.crm.Crmpotenciales();
		  return this.idcrmpotenciales; 
	}
	
	public Mktmlistnews getIdmktmlistnews() {
		if(this.idmktmlistnews==null)this.idmktmlistnews=new org.suinsit.apps.marketing.Mktmlistnews();
		  return this.idmktmlistnews; 
	} 

}