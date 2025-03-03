package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.seguros.Segmgarantia;
import org.suinsit.apps.seguros.Segmramo;

@Entity
@Table (
	name = "SEGRRAMOGAR" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegrramogar" 
)
public class Segrramogar implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXSEGRRAMOGAR",
		nullable = true 
	)
	private Long idxsegrramogar;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMRAMO0",
		referencedColumnName = "IDXSEGMRAMO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmramo idsegmramo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMGARANTIA0",
		referencedColumnName = "IDXSEGMGARANTIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmgarantia idsegmgarantia; 

	public void setIdxsegrramogar(
		Long var
	) {
		this.idxsegrramogar=var; 
	}
	
	public Long getIdxsegrramogar() {
		return this.idxsegrramogar; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmramo(
		Segmramo var
	) {
		this.idsegmramo=var; 
	}
	
	public Segmramo getIdsegmramo() {
		if(this.idsegmramo==null)this.idsegmramo=new org.suinsit.apps.seguros.Segmramo();
		  return this.idsegmramo; 
	}
	
	public void setIdsegmgarantia(
		Segmgarantia var
	) {
		this.idsegmgarantia=var; 
	}
	
	public Segmgarantia getIdsegmgarantia() {
		if(this.idsegmgarantia==null)this.idsegmgarantia=new org.suinsit.apps.seguros.Segmgarantia();
		  return this.idsegmgarantia; 
	} 

}