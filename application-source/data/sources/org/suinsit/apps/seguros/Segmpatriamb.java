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
	name = "SEGMPATRIAMB" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmpatriamb" 
)
public class Segmpatriamb implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "AMBITO",
		nullable = true 
	)
	private String ambito;
	@Id
	@Column (
		name = "IDXSEGMPATRIAMB",
		nullable = true 
	)
	private Long idxsegmpatriamb;
	private boolean updatable; 

	public void setAmbito(
		String var
	) {
		this.ambito=var; 
	}
	
	public String getAmbito() {
		return this.ambito; 
	}
	
	public void setIdxsegmpatriamb(
		Long var
	) {
		this.idxsegmpatriamb=var; 
	}
	
	public Long getIdxsegmpatriamb() {
		return this.idxsegmpatriamb; 
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