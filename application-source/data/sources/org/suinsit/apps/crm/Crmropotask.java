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
import org.suinsit.apps.crm.Crmtarea;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMROPOTASK" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMROPOTASK",
	pk = "idxcrmropotask" 
)
public class Crmropotask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmropotask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmropotask;
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
		name = "IDCRMTAREA0",
		referencedColumnName = "IDXCRMTAREA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtarea idcrmtarea; 

	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmtarea getIdcrmtarea() {
		if(this.idcrmtarea==null)this.idcrmtarea=new org.suinsit.apps.crm.Crmtarea();
		  return this.idcrmtarea; 
	} 

}