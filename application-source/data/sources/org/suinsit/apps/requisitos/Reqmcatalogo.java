package org.suinsit.apps.requisitos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;

@Entity
@Table (
	name = "REQMCATALOGO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxreqmcatalogo" 
)
public class Reqmcatalogo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "PLANTILLA",
		nullable = true 
	)
	private Object plantilla;
	@Column (
		name = "DOCUMENTO",
		nullable = true 
	)
	private Object documento;
	@Column (
		name = "ALTA",
		nullable = false 
	)
	private Date alta;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "BLOQUEADO",
		nullable = true 
	)
	private boolean bloqueado;
	@Column (
		name = "REQUISITO",
		nullable = false 
	)
	private String requisito;
	@Id
	@Column (
		name = "IDXREQMCATALOGO",
		nullable = true 
	)
	private Long idxreqmcatalogo;
	@Column (
		name = "COUSERALTA",
		nullable = true 
	)
	private String couseralta;
	@Column (
		name = "TMALTA",
		nullable = true 
	)
	private Timestamp tmalta;
	@Column (
		name = "COUSERMODIF",
		nullable = true 
	)
	private String cousermodif;
	@Column (
		name = "TMMODIF",
		nullable = true 
	)
	private Timestamp tmmodif;
	private boolean updatable; 

	public void setPlantilla(
		Object var
	) {
		this.plantilla=var; 
	}
	
	public Object getPlantilla() {
		return this.plantilla; 
	}
	
	public void setDocumento(
		Object var
	) {
		this.documento=var; 
	}
	
	public Object getDocumento() {
		return this.documento; 
	}
	
	public void setAlta(
		Date var
	) {
		this.alta=var; 
	}
	
	public Date getAlta() {
		return this.alta; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setBloqueado(
		boolean var
	) {
		this.bloqueado=var; 
	}
	
	public boolean isBloqueado() {
		return this.bloqueado; 
	}
	
	public void setRequisito(
		String var
	) {
		this.requisito=var; 
	}
	
	public String getRequisito() {
		return this.requisito; 
	}
	
	public void setIdxreqmcatalogo(
		Long var
	) {
		this.idxreqmcatalogo=var; 
	}
	
	public Long getIdxreqmcatalogo() {
		return this.idxreqmcatalogo; 
	}
	
	public void setCouseralta(
		String var
	) {
		this.couseralta=var; 
	}
	
	public String getCouseralta() {
		return this.couseralta; 
	}
	
	public void setTmalta(
		Timestamp var
	) {
		this.tmalta=var; 
	}
	
	public Timestamp getTmalta() {
		return this.tmalta; 
	}
	
	public void setCousermodif(
		String var
	) {
		this.cousermodif=var; 
	}
	
	public String getCousermodif() {
		return this.cousermodif; 
	}
	
	public void setTmmodif(
		Timestamp var
	) {
		this.tmmodif=var; 
	}
	
	public Timestamp getTmmodif() {
		return this.tmmodif; 
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