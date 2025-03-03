package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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

@Entity
@Table (
	name = "SEGMCUSTOMER" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmcustomer" 
)
public class Segmcustomer implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CODCLIENTE",
		nullable = false 
	)
	@Sequence (
		name = "SEGMCUSTOMER_CODCLIENTE",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codcliente;
	@Id
	@Column (
		name = "IDXSEGMCUSTOMER",
		nullable = true 
	)
	private Long idxsegmcustomer;
	@Column (
		name = "LISTAEMAIL",
		nullable = true 
	)
	private boolean listaemail;
	@Column (
		name = "LOCKPORTAL",
		nullable = true 
	)
	private boolean lockportal;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSERPORTAL0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa iduserportal;
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

	public void setCodcliente(
		String var
	) {
		this.codcliente=var; 
	}
	
	public String getCodcliente() {
		return this.codcliente; 
	}
	
	public void setIdxsegmcustomer(
		Long var
	) {
		this.idxsegmcustomer=var; 
	}
	
	public Long getIdxsegmcustomer() {
		return this.idxsegmcustomer; 
	}
	
	public void setListaemail(
		boolean var
	) {
		this.listaemail=var; 
	}
	
	public boolean isListaemail() {
		return this.listaemail; 
	}
	
	public void setLockportal(
		boolean var
	) {
		this.lockportal=var; 
	}
	
	public boolean isLockportal() {
		return this.lockportal; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIduserportal(
		Crmempresa var
	) {
		this.iduserportal=var; 
	}
	
	public Crmempresa getIduserportal() {
		if(this.iduserportal==null)this.iduserportal=new org.suinsit.apps.crm.Crmempresa();
		  return this.iduserportal; 
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

}