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
	name = "SEGMREGIMENPATRI" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmregimenpatri" 
)
public class Segmregimenpatri implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "REGIMEN",
		nullable = true 
	)
	private String regimen;
	@Id
	@Column (
		name = "IDXSEGMREGIMENPATRI",
		nullable = true 
	)
	private Long idxsegmregimenpatri;
	private boolean updatable; 

	public void setRegimen(
		String var
	) {
		this.regimen=var; 
	}
	
	public String getRegimen() {
		return this.regimen; 
	}
	
	public void setIdxsegmregimenpatri(
		Long var
	) {
		this.idxsegmregimenpatri=var; 
	}
	
	public Long getIdxsegmregimenpatri() {
		return this.idxsegmregimenpatri; 
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