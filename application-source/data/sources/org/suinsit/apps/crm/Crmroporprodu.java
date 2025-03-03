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
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMROPORPRODU" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMROPORPRODU",
	pk = "idxcrmroporprodu" 
)
public class Crmroporprodu implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmroporprodu",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmroporprodu;
	private boolean updatable;
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
		name = "IDCRMPRODUCTO0",
		referencedColumnName = "IDXCRMPRODUCTO",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmproducto idcrmproducto; 

	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmproducto getIdcrmproducto() {
		if(this.idcrmproducto==null)this.idcrmproducto=new org.suinsit.apps.crm.Crmproducto();
		  return this.idcrmproducto; 
	} 

}