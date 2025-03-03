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
import org.suinsit.apps.marketing.Mktmproducto;
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRPOTPROD" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRPOTPROD",
	labelMonitor = "",
	pk = "idxmktrpotprod" 
)
public class Mktrpotprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmktrpotprod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrpotprod;
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
		name = "IDMKTMPRODUCTO0",
		referencedColumnName = "IDXMKTMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmproducto idmktmproducto; 

	public Mktpotenciales getIdmktpotenciales() {
		if(this.idmktpotenciales==null)this.idmktpotenciales=new org.suinsit.apps.marketing.Mktpotenciales();
		  return this.idmktpotenciales; 
	}
	
	public Mktmproducto getIdmktmproducto() {
		if(this.idmktmproducto==null)this.idmktmproducto=new org.suinsit.apps.marketing.Mktmproducto();
		  return this.idmktmproducto; 
	} 

}