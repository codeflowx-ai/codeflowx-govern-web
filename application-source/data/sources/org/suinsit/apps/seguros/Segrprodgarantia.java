package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.seguros.Segmgarantia;
import org.suinsit.apps.seguros.Segmproducto;

@Entity
@Table (
	name = "SEGRPRODGARANTIA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegrprodgarantia" 
)
public class Segrprodgarantia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "IMPORTE",
		nullable = true 
	)
	private BigDecimal importe;
	@Column (
		name = "INCLUIDO",
		nullable = true 
	)
	private boolean incluido;
	@Column (
		name = "VALOR",
		nullable = true 
	)
	private BigDecimal valor;
	@Id
	@Column (
		name = "IDXSEGRPRODGARANTIA",
		nullable = true 
	)
	private Long idxsegrprodgarantia;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPRODUCTO0",
		referencedColumnName = "IDXSEGMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmproducto idsegmproducto;
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

	public void setImporte(
		BigDecimal var
	) {
		this.importe=var; 
	}
	
	public BigDecimal getImporte() {
		return this.importe; 
	}
	
	public void setIncluido(
		boolean var
	) {
		this.incluido=var; 
	}
	
	public boolean isIncluido() {
		return this.incluido; 
	}
	
	public void setValor(
		BigDecimal var
	) {
		this.valor=var; 
	}
	
	public BigDecimal getValor() {
		return this.valor; 
	}
	
	public void setIdxsegrprodgarantia(
		Long var
	) {
		this.idxsegrprodgarantia=var; 
	}
	
	public Long getIdxsegrprodgarantia() {
		return this.idxsegrprodgarantia; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmproducto(
		Segmproducto var
	) {
		this.idsegmproducto=var; 
	}
	
	public Segmproducto getIdsegmproducto() {
		if(this.idsegmproducto==null)this.idsegmproducto=new org.suinsit.apps.seguros.Segmproducto();
		  return this.idsegmproducto; 
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