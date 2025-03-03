package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.contratos.Crtmcontrato;
import org.suinsit.apps.crm.Crmproducto;

@Entity
@Table (
	name = "CRTRCONTSERV" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcrtrcontserv" 
)
public class Crtrcontserv implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "IMPORTE",
		nullable = true 
	)
	private BigDecimal importe;
	@Column (
		name = "UNIDAD",
		nullable = true 
	)
	private Integer unidad;
	@Id
	@Column (
		name = "IDXCRTRCONTSERV",
		nullable = true 
	)
	private Long idxcrtrcontserv;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRTMCONTRATO0",
		referencedColumnName = "IDXCRTMCONTRATO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crtmcontrato idcrtmcontrato;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMPRODUCTO0",
		referencedColumnName = "IDXCRMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmproducto idcrmproducto; 

	public void setImporte(
		BigDecimal var
	) {
		this.importe=var; 
	}
	
	public BigDecimal getImporte() {
		return this.importe; 
	}
	
	public void setUnidad(
		Integer var
	) {
		this.unidad=var; 
	}
	
	public Integer getUnidad() {
		return this.unidad; 
	}
	
	public void setIdxcrtrcontserv(
		Long var
	) {
		this.idxcrtrcontserv=var; 
	}
	
	public Long getIdxcrtrcontserv() {
		return this.idxcrtrcontserv; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdcrtmcontrato(
		Crtmcontrato var
	) {
		this.idcrtmcontrato=var; 
	}
	
	public Crtmcontrato getIdcrtmcontrato() {
		if(this.idcrtmcontrato==null)this.idcrtmcontrato=new org.suinsit.apps.contratos.Crtmcontrato();
		  return this.idcrtmcontrato; 
	}
	
	public void setIdcrmproducto(
		Crmproducto var
	) {
		this.idcrmproducto=var; 
	}
	
	public Crmproducto getIdcrmproducto() {
		if(this.idcrmproducto==null)this.idcrmproducto=new org.suinsit.apps.crm.Crmproducto();
		  return this.idcrmproducto; 
	} 

}