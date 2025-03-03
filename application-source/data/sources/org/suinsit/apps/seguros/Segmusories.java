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
	name = "SEGMUSORIES" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmusories" 
)
public class Segmusories implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "USORIESGO",
		nullable = false 
	)
	private String usoriesgo;
	@Id
	@Column (
		name = "IDXSEGMUSORIES",
		nullable = true 
	)
	private Long idxsegmusories;
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

	public void setUsoriesgo(
		String var
	) {
		this.usoriesgo=var; 
	}
	
	public String getUsoriesgo() {
		return this.usoriesgo; 
	}
	
	public void setIdxsegmusories(
		Long var
	) {
		this.idxsegmusories=var; 
	}
	
	public Long getIdxsegmusories() {
		return this.idxsegmusories; 
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