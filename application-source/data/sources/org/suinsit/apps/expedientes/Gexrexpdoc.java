package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.expedientes.Gexmepediente;

@Entity
@Table (
	name = "GEXREXPDOC" 
)
@Entidad (
	type = "TABLE",
	pk = "idxgexrexpdoc" 
)
public class Gexrexpdoc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "IDXGEXREXPDOC",
		nullable = true 
	)
	private Long idxgexrexpdoc;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMEPEDIENTE0",
		referencedColumnName = "IDXGEXMEPEDIENTE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmepediente idgexmepediente;
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

	public void setIdxgexrexpdoc(
		Long var
	) {
		this.idxgexrexpdoc=var; 
	}
	
	public Long getIdxgexrexpdoc() {
		return this.idxgexrexpdoc; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdgexmepediente(
		Gexmepediente var
	) {
		this.idgexmepediente=var; 
	}
	
	public Gexmepediente getIdgexmepediente() {
		if(this.idgexmepediente==null)this.idgexmepediente=new org.suinsit.apps.expedientes.Gexmepediente();
		  return this.idgexmepediente; 
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