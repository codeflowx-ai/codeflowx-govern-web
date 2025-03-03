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
	name = "SEGMUSOPATR" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmusopatr" 
)
public class Segmusopatr implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "USOPATRIMONIO",
		nullable = true 
	)
	private String usopatrimonio;
	@Id
	@Column (
		name = "IDXSEGMUSOPATR",
		nullable = true 
	)
	private Long idxsegmusopatr;
	private boolean updatable; 

	public void setUsopatrimonio(
		String var
	) {
		this.usopatrimonio=var; 
	}
	
	public String getUsopatrimonio() {
		return this.usopatrimonio; 
	}
	
	public void setIdxsegmusopatr(
		Long var
	) {
		this.idxsegmusopatr=var; 
	}
	
	public Long getIdxsegmusopatr() {
		return this.idxsegmusopatr; 
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