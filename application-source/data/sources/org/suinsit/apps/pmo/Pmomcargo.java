package org.suinsit.apps.pmo;

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
	name = "PMOMCARGO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxpmomcargo" 
)
public class Pmomcargo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CARGO",
		nullable = true 
	)
	private String cargo;
	@Id
	@Column (
		name = "IDXPMOMCARGO",
		nullable = true 
	)
	private Long idxpmomcargo;
	private boolean updatable; 

	public void setCargo(
		String var
	) {
		this.cargo=var; 
	}
	
	public String getCargo() {
		return this.cargo; 
	}
	
	public void setIdxpmomcargo(
		Long var
	) {
		this.idxpmomcargo=var; 
	}
	
	public Long getIdxpmomcargo() {
		return this.idxpmomcargo; 
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