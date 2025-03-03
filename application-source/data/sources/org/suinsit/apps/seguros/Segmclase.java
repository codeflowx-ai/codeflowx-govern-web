package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.seguros.Segmtiporiesgo;

@Entity
@Table (
	name = "SEGMCLASE" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmclase" 
)
public class Segmclase implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CLASE",
		nullable = true 
	)
	private String clase;
	@Id
	@Column (
		name = "IDXSEGMCLASE",
		nullable = true 
	)
	private Long idxsegmclase;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPORIESGO0",
		referencedColumnName = "IDXSEGMTIPORIESGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtiporiesgo idsegmtiporiesgo; 

	public void setClase(
		String var
	) {
		this.clase=var; 
	}
	
	public String getClase() {
		return this.clase; 
	}
	
	public void setIdxsegmclase(
		Long var
	) {
		this.idxsegmclase=var; 
	}
	
	public Long getIdxsegmclase() {
		return this.idxsegmclase; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmtiporiesgo(
		Segmtiporiesgo var
	) {
		this.idsegmtiporiesgo=var; 
	}
	
	public Segmtiporiesgo getIdsegmtiporiesgo() {
		if(this.idsegmtiporiesgo==null)this.idsegmtiporiesgo=new org.suinsit.apps.seguros.Segmtiporiesgo();
		  return this.idsegmtiporiesgo; 
	} 

}