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
	name = "SEGMCLAVE" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmclave" 
)
public class Segmclave implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CLAVE",
		nullable = true 
	)
	private String clave;
	@Id
	@Column (
		name = "IDXSEGMCLAVE",
		nullable = true 
	)
	private Long idxsegmclave;
	private boolean updatable; 

	public void setClave(
		String var
	) {
		this.clave=var; 
	}
	
	public String getClave() {
		return this.clave; 
	}
	
	public void setIdxsegmclave(
		Long var
	) {
		this.idxsegmclave=var; 
	}
	
	public Long getIdxsegmclave() {
		return this.idxsegmclave; 
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