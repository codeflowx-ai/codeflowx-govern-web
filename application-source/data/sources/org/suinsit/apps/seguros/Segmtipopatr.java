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
	name = "SEGMTIPOPATR" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmtipopatr" 
)
public class Segmtipopatr implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "TIPOPATRIMONIO",
		nullable = false 
	)
	private String tipopatrimonio;
	@Id
	@Column (
		name = "IDXSEGMTIPOPATR",
		nullable = true 
	)
	private Long idxsegmtipopatr;
	private boolean updatable; 

	public void setTipopatrimonio(
		String var
	) {
		this.tipopatrimonio=var; 
	}
	
	public String getTipopatrimonio() {
		return this.tipopatrimonio; 
	}
	
	public void setIdxsegmtipopatr(
		Long var
	) {
		this.idxsegmtipopatr=var; 
	}
	
	public Long getIdxsegmtipopatr() {
		return this.idxsegmtipopatr; 
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