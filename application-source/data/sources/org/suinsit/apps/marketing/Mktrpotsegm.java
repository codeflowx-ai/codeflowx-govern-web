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
import org.suinsit.apps.marketing.Mktmsegmento;
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRPOTSEGM" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRPOTSEGM",
	labelMonitor = "",
	pk = "idxmktrpotsegm" 
)
public class Mktrpotsegm implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmktrpotsegm",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrpotsegm;
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
		name = "IDMKTMSEGMENTO0",
		referencedColumnName = "IDXMKTMSEGMENTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmsegmento idmktmsegmento; 

	public Mktpotenciales getIdmktpotenciales() {
		if(this.idmktpotenciales==null)this.idmktpotenciales=new org.suinsit.apps.marketing.Mktpotenciales();
		  return this.idmktpotenciales; 
	}
	
	public Mktmsegmento getIdmktmsegmento() {
		if(this.idmktmsegmento==null)this.idmktmsegmento=new org.suinsit.apps.marketing.Mktmsegmento();
		  return this.idmktmsegmento; 
	} 

}