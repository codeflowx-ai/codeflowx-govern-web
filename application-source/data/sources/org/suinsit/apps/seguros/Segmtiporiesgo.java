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
	name = "SEGMTIPORIESGO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmtiporiesgo" 
)
public class Segmtiporiesgo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "TIPO",
		nullable = true 
	)
	private String tipo;
	@Id
	@Column (
		name = "IDXSEGMTIPORIESGO",
		nullable = true 
	)
	private Long idxsegmtiporiesgo;
	private boolean updatable; 

	public void setTipo(
		String var
	) {
		this.tipo=var; 
	}
	
	public String getTipo() {
		return this.tipo; 
	}
	
	public void setIdxsegmtiporiesgo(
		Long var
	) {
		this.idxsegmtiporiesgo=var; 
	}
	
	public Long getIdxsegmtiporiesgo() {
		return this.idxsegmtiporiesgo; 
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