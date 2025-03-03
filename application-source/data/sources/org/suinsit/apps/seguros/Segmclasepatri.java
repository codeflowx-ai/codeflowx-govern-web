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
import org.suinsit.apps.seguros.Segmtipopatr;

@Entity
@Table (
	name = "SEGMCLASEPATRI" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmclasepatri" 
)
public class Segmclasepatri implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CLASE",
		nullable = false 
	)
	private String clase;
	@Id
	@Column (
		name = "IDXSEGMCLASEPATRI",
		nullable = true 
	)
	private Long idxsegmclasepatri;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOPATR0",
		referencedColumnName = "IDXSEGMTIPOPATR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtipopatr idsegmtipopatr; 

	public void setClase(
		String var
	) {
		this.clase=var; 
	}
	
	public String getClase() {
		return this.clase; 
	}
	
	public void setIdxsegmclasepatri(
		Long var
	) {
		this.idxsegmclasepatri=var; 
	}
	
	public Long getIdxsegmclasepatri() {
		return this.idxsegmclasepatri; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmtipopatr(
		Segmtipopatr var
	) {
		this.idsegmtipopatr=var; 
	}
	
	public Segmtipopatr getIdsegmtipopatr() {
		if(this.idsegmtipopatr==null)this.idsegmtipopatr=new org.suinsit.apps.seguros.Segmtipopatr();
		  return this.idsegmtipopatr; 
	} 

}