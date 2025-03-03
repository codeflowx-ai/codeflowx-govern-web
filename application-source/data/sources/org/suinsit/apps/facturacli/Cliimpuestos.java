package org.suinsit.apps.facturacli;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.crm.Crmempresa;

@Entity
@Table (
	name = "CLIIMPUESTOS" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcliimpuestos" 
)
public class Cliimpuestos implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ACTIVADO",
		nullable = true 
	)
	private boolean activado;
	@Id
	@Column (
		name = "IDXCLIIMPUESTOS",
		nullable = true 
	)
	private Long idxcliimpuestos;
	@Column (
		name = "IMPUESTO",
		nullable = true 
	)
	private String impuesto;
	@Column (
		name = "IVA",
		nullable = true 
	)
	private BigDecimal iva;
	@Column (
		name = "RETENCION",
		nullable = true 
	)
	private boolean retencion;
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

	public void setActivado(
		boolean var
	) {
		this.activado=var; 
	}
	
	public boolean isActivado() {
		return this.activado; 
	}
	
	public void setIdxcliimpuestos(
		Long var
	) {
		this.idxcliimpuestos=var; 
	}
	
	public Long getIdxcliimpuestos() {
		return this.idxcliimpuestos; 
	}
	
	public void setImpuesto(
		String var
	) {
		this.impuesto=var; 
	}
	
	public String getImpuesto() {
		return this.impuesto; 
	}
	
	public void setIva(
		BigDecimal var
	) {
		this.iva=var; 
	}
	
	public BigDecimal getIva() {
		return this.iva; 
	}
	
	public void setRetencion(
		boolean var
	) {
		this.retencion=var; 
	}
	
	public boolean isRetencion() {
		return this.retencion; 
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