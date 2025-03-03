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
	name = "SEGMZONAPATRI" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmzonapatri" 
)
public class Segmzonapatri implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ZONA",
		nullable = true 
	)
	private String zona;
	@Id
	@Column (
		name = "IDXSEGMZONAPATRI",
		nullable = true 
	)
	private Long idxsegmzonapatri;
	private boolean updatable; 

	public void setZona(
		String var
	) {
		this.zona=var; 
	}
	
	public String getZona() {
		return this.zona; 
	}
	
	public void setIdxsegmzonapatri(
		Long var
	) {
		this.idxsegmzonapatri=var; 
	}
	
	public Long getIdxsegmzonapatri() {
		return this.idxsegmzonapatri; 
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