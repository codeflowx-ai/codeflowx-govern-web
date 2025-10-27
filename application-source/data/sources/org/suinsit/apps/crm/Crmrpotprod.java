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
import org.suinsit.apps.crm.Crmproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMRPOTPROD" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMRPOTPROD",
	labelMonitor = "",
	pk = "idxcrmrpotprod" 
)
public class Crmrpotprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmrpotprod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmrpotprod;
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
		name = "IDCRMPRODUCTO0",
		referencedColumnName = "IDXCRMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmproducto idcrmproducto; 

	public Crmpotenciales getIdcrmpotenciales() {
		if(this.idcrmpotenciales==null)this.idcrmpotenciales=new org.suinsit.apps.crm.Crmpotenciales();
		  return this.idcrmpotenciales; 
	}
	
	public Crmproducto getIdcrmproducto() {
		if(this.idcrmproducto==null)this.idcrmproducto=new org.suinsit.apps.crm.Crmproducto();
		  return this.idcrmproducto; 
	} 

}