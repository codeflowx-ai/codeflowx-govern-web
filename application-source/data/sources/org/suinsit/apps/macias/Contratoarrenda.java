package org.suinsit.apps.macias;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
	name = "CONTRATOARRENDA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcontratoarrenda" 
)
public class Contratoarrenda implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "PRECIO",
		nullable = true 
	)
	private BigDecimal precio;
	@Column (
		name = "ALTA",
		nullable = true 
	)
	private Timestamp alta;
	@Column (
		name = "REFERENCIA",
		nullable = true 
	)
	@Sequence (
		name = "CONTRATOARRENDA_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String referencia;
	@Id
	@Column (
		name = "IDXCONTRATOARRENDA",
		nullable = true 
	)
	private Long idxcontratoarrenda;
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

	public void setPrecio(
		BigDecimal var
	) {
		this.precio=var; 
	}
	
	public BigDecimal getPrecio() {
		return this.precio; 
	}
	
	public void setAlta(
		Timestamp var
	) {
		this.alta=var; 
	}
	
	public Timestamp getAlta() {
		return this.alta; 
	}
	
	public void setReferencia(
		String var
	) {
		this.referencia=var; 
	}
	
	public String getReferencia() {
		return this.referencia; 
	}
	
	public void setIdxcontratoarrenda(
		Long var
	) {
		this.idxcontratoarrenda=var; 
	}
	
	public Long getIdxcontratoarrenda() {
		return this.idxcontratoarrenda; 
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