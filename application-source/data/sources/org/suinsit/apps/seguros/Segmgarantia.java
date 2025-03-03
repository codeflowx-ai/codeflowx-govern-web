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
	name = "SEGMGARANTIA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmgarantia" 
)
public class Segmgarantia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "GARANTIA",
		nullable = false 
	)
	private String garantia;
	@Id
	@Column (
		name = "IDXSEGMGARANTIA",
		nullable = true 
	)
	private Long idxsegmgarantia;
	private boolean updatable; 

	public void setGarantia(
		String var
	) {
		this.garantia=var; 
	}
	
	public String getGarantia() {
		return this.garantia; 
	}
	
	public void setIdxsegmgarantia(
		Long var
	) {
		this.idxsegmgarantia=var; 
	}
	
	public Long getIdxsegmgarantia() {
		return this.idxsegmgarantia; 
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