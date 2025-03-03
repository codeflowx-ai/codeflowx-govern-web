package org.suinsit.apps.document;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Id;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "QUERY",
	pk = "idxdoccatalogo" 
)
public class Querycatamodulo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXDOCCATALOGO",
		nullable = true 
	)
	private Long idxdoccatalogo;
	@Column (
		name = "CATALOGO",
		nullable = false 
	)
	private String catalogo;
	@Column (
		name = "CARPETA",
		nullable = true 
	)
	private String carpeta;
	@Column (
		name = "AGRUPAR",
		nullable = true 
	)
	private String agrupar;
	@Column (
		name = "ACTIVO",
		nullable = true 
	)
	private boolean activo;
	@Column (
		name = "ACCESOCLI",
		nullable = true 
	)
	private boolean accesocli;
	@Column (
		name = "IDMMODULO0",
		nullable = true 
	)
	private Long idmmodulo0;
	private boolean updatable; 

	public void setIdxdoccatalogo(
		Long var
	) {
		this.idxdoccatalogo=var; 
	}
	
	public Long getIdxdoccatalogo() {
		return this.idxdoccatalogo; 
	}
	
	public void setCatalogo(
		String var
	) {
		this.catalogo=var; 
	}
	
	public String getCatalogo() {
		return this.catalogo; 
	}
	
	public void setCarpeta(
		String var
	) {
		this.carpeta=var; 
	}
	
	public String getCarpeta() {
		return this.carpeta; 
	}
	
	public void setAgrupar(
		String var
	) {
		this.agrupar=var; 
	}
	
	public String getAgrupar() {
		return this.agrupar; 
	}
	
	public void setActivo(
		boolean var
	) {
		this.activo=var; 
	}
	
	public boolean isActivo() {
		return this.activo; 
	}
	
	public void setAccesocli(
		boolean var
	) {
		this.accesocli=var; 
	}
	
	public boolean isAccesocli() {
		return this.accesocli; 
	}
	
	public void setIdmmodulo0(
		Long var
	) {
		this.idmmodulo0=var; 
	}
	
	public Long getIdmmodulo0() {
		return this.idmmodulo0; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	} 

}