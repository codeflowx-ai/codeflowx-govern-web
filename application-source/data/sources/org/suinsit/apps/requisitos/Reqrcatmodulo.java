package org.suinsit.apps.requisitos;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.requisitos.Reqmcatalogo;

@Entity
@Table (
	name = "REQRCATMODULO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxreqrcatmodulo" 
)
public class Reqrcatmodulo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ALTA",
		nullable = false 
	)
	private Date alta;
	@Column (
		name = "COMENTARIOS",
		nullable = true 
	)
	private String comentarios;
	@Column (
		name = "CUMPLI",
		nullable = true 
	)
	private Timestamp cumpli;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "FILE",
		nullable = true 
	)
	private Object file;
	@Column (
		name = "IDTUPLA",
		nullable = true 
	)
	private Integer idtupla;
	@Id
	@Column (
		name = "IDXREQRCATMODULO",
		nullable = true 
	)
	private Long idxreqrcatmodulo;
	@Column (
		name = "MODULE",
		nullable = true 
	)
	private String module;
	@Column (
		name = "REFERENCIA",
		nullable = true 
	)
	@Sequence (
		name = "REQRCATMODULO_REFERENCIA",
		prefix = "",
		mask = "0000000000",
		addYear = true 
	)
	private String referencia;
	@Column (
		name = "REQUIRED",
		nullable = true 
	)
	private boolean required;
	@Column (
		name = "URLPATH",
		nullable = true 
	)
	private String urlpath;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDREQMCATALOGO0",
		referencedColumnName = "IDXREQMCATALOGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Reqmcatalogo idreqmcatalogo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSERALTA0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario iduseralta; 

	public void setAlta(
		Date var
	) {
		this.alta=var; 
	}
	
	public Date getAlta() {
		return this.alta; 
	}
	
	public void setComentarios(
		String var
	) {
		this.comentarios=var; 
	}
	
	public String getComentarios() {
		return this.comentarios; 
	}
	
	public void setCumpli(
		Timestamp var
	) {
		this.cumpli=var; 
	}
	
	public Timestamp getCumpli() {
		return this.cumpli; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setFile(
		Object var
	) {
		this.file=var; 
	}
	
	public Object getFile() {
		return this.file; 
	}
	
	public void setIdtupla(
		Integer var
	) {
		this.idtupla=var; 
	}
	
	public Integer getIdtupla() {
		return this.idtupla; 
	}
	
	public void setIdxreqrcatmodulo(
		Long var
	) {
		this.idxreqrcatmodulo=var; 
	}
	
	public Long getIdxreqrcatmodulo() {
		return this.idxreqrcatmodulo; 
	}
	
	public void setModule(
		String var
	) {
		this.module=var; 
	}
	
	public String getModule() {
		return this.module; 
	}
	
	public void setReferencia(
		String var
	) {
		this.referencia=var; 
	}
	
	public String getReferencia() {
		return this.referencia; 
	}
	
	public void setRequired(
		boolean var
	) {
		this.required=var; 
	}
	
	public boolean isRequired() {
		return this.required; 
	}
	
	public void setUrlpath(
		String var
	) {
		this.urlpath=var; 
	}
	
	public String getUrlpath() {
		return this.urlpath; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdreqmcatalogo(
		Reqmcatalogo var
	) {
		this.idreqmcatalogo=var; 
	}
	
	public Reqmcatalogo getIdreqmcatalogo() {
		if(this.idreqmcatalogo==null)this.idreqmcatalogo=new org.suinsit.apps.requisitos.Reqmcatalogo();
		  return this.idreqmcatalogo; 
	}
	
	public void setIdssousuario(
		Ssousuario var
	) {
		this.idssousuario=var; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
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
	
	public void setIduseralta(
		Ssousuario var
	) {
		this.iduseralta=var; 
	}
	
	public Ssousuario getIduseralta() {
		if(this.iduseralta==null)this.iduseralta=new org.suinsit.apps.admin.Ssousuario();
		  return this.iduseralta; 
	} 

}