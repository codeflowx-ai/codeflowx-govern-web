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
	name = "SEGMTIPOPROV" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmtipoprov" 
)
public class Segmtipoprov implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "TIPOPROVEEDOR",
		nullable = true 
	)
	private String tipoproveedor;
	@Id
	@Column (
		name = "IDXSEGMTIPOPROV",
		nullable = true 
	)
	private Long idxsegmtipoprov;
	private boolean updatable; 

	public void setTipoproveedor(
		String var
	) {
		this.tipoproveedor=var; 
	}
	
	public String getTipoproveedor() {
		return this.tipoproveedor; 
	}
	
	public void setIdxsegmtipoprov(
		Long var
	) {
		this.idxsegmtipoprov=var; 
	}
	
	public Long getIdxsegmtipoprov() {
		return this.idxsegmtipoprov; 
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