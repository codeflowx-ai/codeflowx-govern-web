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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.crm.Crmempresa;

@Entity
@Table (
	name = "SEGMPERITO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmperito" 
)
public class Segmperito implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CODPERITO",
		nullable = false 
	)
	@Sequence (
		name = "SEGMPERITO_CODPERITO",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codperito;
	@Id
	@Column (
		name = "IDXSEGMPERITO",
		nullable = true 
	)
	private Long idxsegmperito;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa; 

	public void setCodperito(
		String var
	) {
		this.codperito=var; 
	}
	
	public String getCodperito() {
		return this.codperito; 
	}
	
	public void setIdxsegmperito(
		Long var
	) {
		this.idxsegmperito=var; 
	}
	
	public Long getIdxsegmperito() {
		return this.idxsegmperito; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdcrmempresa(
		Crmempresa var
	) {
		this.idcrmempresa=var; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	} 

}