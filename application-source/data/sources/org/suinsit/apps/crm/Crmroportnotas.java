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
import org.suinsit.apps.crm.Crmnota;
import org.suinsit.apps.crm.Crmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMROPORTNOTAS" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMROPORTNOTAS",
	pk = "idxcrmroportnotas" 
)
public class Crmroportnotas implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmroportnotas",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmroportnotas;
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
		name = "IDCRMNOTA0",
		referencedColumnName = "IDXCRMNOTA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmnota idcrmnota; 

	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmnota getIdcrmnota() {
		if(this.idcrmnota==null)this.idcrmnota=new org.suinsit.apps.crm.Crmnota();
		  return this.idcrmnota; 
	} 

}