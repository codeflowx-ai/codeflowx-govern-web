package org.suinsit.apps.franchise;

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
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.franchise.Frcmfranchise;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FRCRPROVFRANC" 
)
@Entidad (
	namespace = "franchise",
	type = "TABLE",
	name = "FRCRPROVFRANC",
	labelMonitor = "",
	pk = "idxfrcrprovfranc" 
)
public class Frcrprovfranc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxfrcrprovfranc",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfrcrprovfranc;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFRCMFRANCHISE0",
		referencedColumnName = "IDXFRCMFRANCHISE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frcmfranchise idfrcmfranchise;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPROVINCIA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mprovincia idmprovincia; 

	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	} 

}