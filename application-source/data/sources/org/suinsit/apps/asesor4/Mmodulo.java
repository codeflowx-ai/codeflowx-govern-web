package org.suinsit.apps.asesor4;

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
	name = "MMODULO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxmmodulo" 
)
public class Mmodulo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "LABEL",
		nullable = true 
	)
	private String label;
	@Column (
		name = "MODULO",
		nullable = true 
	)
	private String modulo;
	@Id
	@Column (
		name = "IDXMMODULO",
		nullable = true 
	)
	private Long idxmmodulo;
	private boolean updatable; 

	public void setLabel(
		String var
	) {
		this.label=var; 
	}
	
	public String getLabel() {
		return this.label; 
	}
	
	public void setModulo(
		String var
	) {
		this.modulo=var; 
	}
	
	public String getModulo() {
		return this.modulo; 
	}
	
	public void setIdxmmodulo(
		Long var
	) {
		this.idxmmodulo=var; 
	}
	
	public Long getIdxmmodulo() {
		return this.idxmmodulo; 
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