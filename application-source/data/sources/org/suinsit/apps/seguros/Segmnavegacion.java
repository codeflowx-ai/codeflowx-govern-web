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
	name = "SEGMNAVEGACION" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmnavegacion" 
)
public class Segmnavegacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ZONANAVEGA",
		nullable = false 
	)
	private String zonanavega;
	@Id
	@Column (
		name = "IDXSEGMNAVEGACION",
		nullable = true 
	)
	private Long idxsegmnavegacion;
	private boolean updatable; 

	public void setZonanavega(
		String var
	) {
		this.zonanavega=var; 
	}
	
	public String getZonanavega() {
		return this.zonanavega; 
	}
	
	public void setIdxsegmnavegacion(
		Long var
	) {
		this.idxsegmnavegacion=var; 
	}
	
	public Long getIdxsegmnavegacion() {
		return this.idxsegmnavegacion; 
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