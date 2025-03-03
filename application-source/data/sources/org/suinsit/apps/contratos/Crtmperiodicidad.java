package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.contratos.Crtmcontrato;
import org.suinsit.apps.contratos.Crtmtipo;

@Entity
@Table (
	name = "CRTMPERIODICIDAD" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcrtmperiodicidad" 
)
public class Crtmperiodicidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "DIASRENOVACION",
		nullable = true 
	)
	private BigDecimal diasrenovacion;
	@Id
	@Column (
		name = "IDXCRTMPERIODICIDAD",
		nullable = true 
	)
	private Long idxcrtmperiodicidad;
	@Column (
		name = "PERIODICIDAD",
		nullable = false 
	)
	private String periodicidad;
	private boolean updatable;
	private List<Crtmtipo> subcrtmtipo;
	private List<Crtmcontrato> subcrtmcontrato; 

	public void setDiasrenovacion(
		BigDecimal var
	) {
		this.diasrenovacion=var; 
	}
	
	public BigDecimal getDiasrenovacion() {
		return this.diasrenovacion; 
	}
	
	public void setIdxcrtmperiodicidad(
		Long var
	) {
		this.idxcrtmperiodicidad=var; 
	}
	
	public Long getIdxcrtmperiodicidad() {
		return this.idxcrtmperiodicidad; 
	}
	
	public void setPeriodicidad(
		String var
	) {
		this.periodicidad=var; 
	}
	
	public String getPeriodicidad() {
		return this.periodicidad; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setSubcrtmtipo(
		List<Crtmtipo> var
	) {
		this.subcrtmtipo=var; 
	}
	
	public List<Crtmtipo> getSubcrtmtipo() {
		if(this.subcrtmtipo==null)this.subcrtmtipo=new ArrayList<>(0);
		  return this.subcrtmtipo; 
	}
	
	public void setSubcrtmcontrato(
		List<Crtmcontrato> var
	) {
		this.subcrtmcontrato=var; 
	}
	
	public List<Crtmcontrato> getSubcrtmcontrato() {
		if(this.subcrtmcontrato==null)this.subcrtmcontrato=new ArrayList<>(0);
		  return this.subcrtmcontrato; 
	} 

}