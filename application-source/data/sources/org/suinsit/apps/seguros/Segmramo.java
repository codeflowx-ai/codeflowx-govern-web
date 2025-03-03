package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;

@Entity
@Table (
	name = "SEGMRAMO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmramo" 
)
public class Segmramo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXSEGMRAMO",
		nullable = true 
	)
	private Long idxsegmramo;
	@Column (
		name = "RAMO",
		nullable = false 
	)
	private String ramo;
	@Column (
		name = "RAMODGS",
		nullable = true 
	)
	private String ramodgs;
	private boolean updatable; 

	public void setIdxsegmramo(
		Long var
	) {
		this.idxsegmramo=var; 
	}
	
	public Long getIdxsegmramo() {
		return this.idxsegmramo; 
	}
	
	public void setRamo(
		String var
	) {
		this.ramo=var; 
	}
	
	public String getRamo() {
		return this.ramo; 
	}
	
	public void setRamodgs(
		String var
	) {
		this.ramodgs=var; 
	}
	
	public String getRamodgs() {
		return this.ramodgs; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	} 

}