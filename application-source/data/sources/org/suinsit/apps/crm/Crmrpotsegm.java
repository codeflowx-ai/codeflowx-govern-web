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
import org.suinsit.apps.crm.Crmsegmento;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMRPOTSEGM" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMRPOTSEGM",
	labelMonitor = "",
	pk = "idxcrmrpotsegm" 
)
public class Crmrpotsegm implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrmrpotsegm",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmrpotsegm;
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
		name = "IDCRMSEGMENTO0",
		referencedColumnName = "IDXCRMSEGMENTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmsegmento idcrmsegmento; 

	public Crmpotenciales getIdcrmpotenciales() {
		if(this.idcrmpotenciales==null)this.idcrmpotenciales=new org.suinsit.apps.crm.Crmpotenciales();
		  return this.idcrmpotenciales; 
	}
	
	public Crmsegmento getIdcrmsegmento() {
		if(this.idcrmsegmento==null)this.idcrmsegmento=new org.suinsit.apps.crm.Crmsegmento();
		  return this.idcrmsegmento; 
	} 

}