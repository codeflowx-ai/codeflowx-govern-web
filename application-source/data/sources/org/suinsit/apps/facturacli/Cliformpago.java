package org.suinsit.apps.facturacli;

import java.io.Serializable;
import java.lang.Integer;
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
	name = "CLIFORMPAGO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcliformpago" 
)
public class Cliformpago implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "DESFACTURA",
		nullable = true 
	)
	private String desfactura;
	@Column (
		name = "DIASFACTURA",
		nullable = true 
	)
	private Integer diasfactura;
	@Column (
		name = "FORMADEPAGO",
		nullable = true 
	)
	private String formadepago;
	@Id
	@Column (
		name = "IDXCLIFORMPAGO",
		nullable = true 
	)
	private Long idxcliformpago;
	@Column (
		name = "MENSUAL",
		nullable = true 
	)
	private boolean mensual;
	@Column (
		name = "NUMPAGOS",
		nullable = true 
	)
	private Integer numpagos;
	@Column (
		name = "PORCENTAJE",
		nullable = true 
	)
	private BigDecimal porcentaje;
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

	public void setDesfactura(
		String var
	) {
		this.desfactura=var; 
	}
	
	public String getDesfactura() {
		return this.desfactura; 
	}
	
	public void setDiasfactura(
		Integer var
	) {
		this.diasfactura=var; 
	}
	
	public Integer getDiasfactura() {
		return this.diasfactura; 
	}
	
	public void setFormadepago(
		String var
	) {
		this.formadepago=var; 
	}
	
	public String getFormadepago() {
		return this.formadepago; 
	}
	
	public void setIdxcliformpago(
		Long var
	) {
		this.idxcliformpago=var; 
	}
	
	public Long getIdxcliformpago() {
		return this.idxcliformpago; 
	}
	
	public void setMensual(
		boolean var
	) {
		this.mensual=var; 
	}
	
	public boolean isMensual() {
		return this.mensual; 
	}
	
	public void setNumpagos(
		Integer var
	) {
		this.numpagos=var; 
	}
	
	public Integer getNumpagos() {
		return this.numpagos; 
	}
	
	public void setPorcentaje(
		BigDecimal var
	) {
		this.porcentaje=var; 
	}
	
	public BigDecimal getPorcentaje() {
		return this.porcentaje; 
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