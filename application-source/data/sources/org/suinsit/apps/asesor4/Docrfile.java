package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.admin.Mmodulo;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.portalcli.Pclirdocemp;

@Entity
@Table (
	name = "DOCRFILE" 
)
@Entidad (
	type = "TABLE",
	pk = "idxdocrfile" 
)
public class Docrfile implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "WRITE",
		nullable = true 
	)
	private boolean write;
	@Column (
		name = "READ",
		nullable = true 
	)
	private boolean read;
	@Column (
		name = "LOCK",
		nullable = true 
	)
	private boolean lock;
	@Column (
		name = "IDTUPLA",
		nullable = false 
	)
	private Integer idtupla;
	@Id
	@Column (
		name = "IDXDOCRFILE",
		nullable = true 
	)
	private Long idxdocrfile;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMMODULO0",
		referencedColumnName = "IDXMMODULO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mmodulo idmmodulo;
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
	private List<Pclirdocemp> subrrhhrdocemp; 

	public void setWrite(
		boolean var
	) {
		this.write=var; 
	}
	
	public boolean isWrite() {
		return this.write; 
	}
	
	public void setRead(
		boolean var
	) {
		this.read=var; 
	}
	
	public boolean isRead() {
		return this.read; 
	}
	
	public void setLock(
		boolean var
	) {
		this.lock=var; 
	}
	
	public boolean isLock() {
		return this.lock; 
	}
	
	public void setIdtupla(
		Integer var
	) {
		this.idtupla=var; 
	}
	
	public Integer getIdtupla() {
		return this.idtupla; 
	}
	
	public void setIdxdocrfile(
		Long var
	) {
		this.idxdocrfile=var; 
	}
	
	public Long getIdxdocrfile() {
		return this.idxdocrfile; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdmmodulo(
		Mmodulo var
	) {
		this.idmmodulo=var; 
	}
	
	public Mmodulo getIdmmodulo() {
		if(this.idmmodulo==null)this.idmmodulo=new org.suinsit.apps.admin.Mmodulo();
		  return this.idmmodulo; 
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
	
	public void setSubrrhhrdocemp(
		List<Pclirdocemp> var
	) {
		this.subrrhhrdocemp=var; 
	}
	
	public List<Pclirdocemp> getSubrrhhrdocemp() {
		if(this.subrrhhrdocemp==null)this.subrrhhrdocemp=new ArrayList<>(0);
		  return this.subrrhhrdocemp; 
	} 

}