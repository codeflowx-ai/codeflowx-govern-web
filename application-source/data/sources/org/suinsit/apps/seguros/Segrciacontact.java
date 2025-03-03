package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.seguros.Segmcompany;

@Entity
@Table (
	name = "SEGRCIACONTACT" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegrciacontact" 
)
public class Segrciacontact implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXSEGRCIACONTACT",
		nullable = true 
	)
	private Long idxsegrciacontact;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMCOMPANY0",
		referencedColumnName = "IDXSEGMCOMPANY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmcompany idsegmcompany;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMCONTACTO0",
		referencedColumnName = "IDXCRMCONTACTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmcontacto idcrmcontacto; 

	public void setIdxsegrciacontact(
		Long var
	) {
		this.idxsegrciacontact=var; 
	}
	
	public Long getIdxsegrciacontact() {
		return this.idxsegrciacontact; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmcompany(
		Segmcompany var
	) {
		this.idsegmcompany=var; 
	}
	
	public Segmcompany getIdsegmcompany() {
		if(this.idsegmcompany==null)this.idsegmcompany=new org.suinsit.apps.seguros.Segmcompany();
		  return this.idsegmcompany; 
	}
	
	public void setIdcrmcontacto(
		Crmcontacto var
	) {
		this.idcrmcontacto=var; 
	}
	
	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	} 

}