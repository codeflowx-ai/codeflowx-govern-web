package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.seguros.Segmtipoprod;

@Entity
@Table (
	name = "SEGMTIPOSEG" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmtiposeg" 
)
public class Segmtiposeg implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "TIPOSEGURO",
		nullable = true 
	)
	private String tiposeguro;
	@Id
	@Column (
		name = "IDXSEGMTIPOSEG",
		nullable = true 
	)
	private Long idxsegmtiposeg;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOPROD0",
		referencedColumnName = "IDXSEGMTIPOPROD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtipoprod idsegmtipoprod; 

	public void setTiposeguro(
		String var
	) {
		this.tiposeguro=var; 
	}
	
	public String getTiposeguro() {
		return this.tiposeguro; 
	}
	
	public void setIdxsegmtiposeg(
		Long var
	) {
		this.idxsegmtiposeg=var; 
	}
	
	public Long getIdxsegmtiposeg() {
		return this.idxsegmtiposeg; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmtipoprod(
		Segmtipoprod var
	) {
		this.idsegmtipoprod=var; 
	}
	
	public Segmtipoprod getIdsegmtipoprod() {
		if(this.idsegmtipoprod==null)this.idsegmtipoprod=new org.suinsit.apps.seguros.Segmtipoprod();
		  return this.idsegmtipoprod; 
	} 

}