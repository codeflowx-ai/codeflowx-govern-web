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
import org.suinsit.apps.admin.Mpais;

@Entity
@Table (
	name = "SEGMBANDERA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmbandera" 
)
public class Segmbandera implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "BANDERA",
		nullable = true 
	)
	private String bandera;
	@Id
	@Column (
		name = "IDXSEGMBANDERA",
		nullable = true 
	)
	private Long idxsegmbandera;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPAIS0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idmpais; 

	public void setBandera(
		String var
	) {
		this.bandera=var; 
	}
	
	public String getBandera() {
		return this.bandera; 
	}
	
	public void setIdxsegmbandera(
		Long var
	) {
		this.idxsegmbandera=var; 
	}
	
	public Long getIdxsegmbandera() {
		return this.idxsegmbandera; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdmpais(
		Mpais var
	) {
		this.idmpais=var; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	} 

}