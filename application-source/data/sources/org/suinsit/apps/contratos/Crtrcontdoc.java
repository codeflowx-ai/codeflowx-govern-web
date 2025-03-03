package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.contratos.Crtmcontrato;
import org.suinsit.apps.document.Docfichero;

@Entity
@Table (
	name = "CRTRCONTDOC" 
)
@Entidad (
	type = "TABLE",
	pk = "idxcrtrcontdoc" 
)
public class Crtrcontdoc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXCRTRCONTDOC",
		nullable = true 
	)
	private Long idxcrtrcontdoc;
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
		name = "IDDOCFICHERO0",
		referencedColumnName = "IDXDOCFICHERO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Docfichero iddocfichero; 

	public void setIdxcrtrcontdoc(
		Long var
	) {
		this.idxcrtrcontdoc=var; 
	}
	
	public Long getIdxcrtrcontdoc() {
		return this.idxcrtrcontdoc; 
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
	
	public void setIddocfichero(
		Docfichero var
	) {
		this.iddocfichero=var; 
	}
	
	public Docfichero getIddocfichero() {
		if(this.iddocfichero==null)this.iddocfichero=new org.suinsit.apps.document.Docfichero();
		  return this.iddocfichero; 
	} 

}