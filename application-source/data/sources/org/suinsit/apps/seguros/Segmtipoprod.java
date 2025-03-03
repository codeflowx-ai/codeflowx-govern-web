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
	name = "SEGMTIPOPROD" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmtipoprod" 
)
public class Segmtipoprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "TIPOPROD",
		nullable = false 
	)
	private String tipoprod;
	@Id
	@Column (
		name = "IDXSEGMTIPOPROD",
		nullable = true 
	)
	private Long idxsegmtipoprod;
	private boolean updatable; 

	public void setTipoprod(
		String var
	) {
		this.tipoprod=var; 
	}
	
	public String getTipoprod() {
		return this.tipoprod; 
	}
	
	public void setIdxsegmtipoprod(
		Long var
	) {
		this.idxsegmtipoprod=var; 
	}
	
	public Long getIdxsegmtipoprod() {
		return this.idxsegmtipoprod; 
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