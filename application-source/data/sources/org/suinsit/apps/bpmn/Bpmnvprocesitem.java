package org.suinsit.apps.bpmn;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "VIEW" 
)
public class Bpmnvprocesitem implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "aa.EMAILSBCC",
		nullable = true 
	)
	private String aa.emailsbcc;
	@Column (
		name = "aa.EMAILSCC",
		nullable = true 
	)
	private String aa.emailscc;
	@Column (
		name = "aa.EMAILS",
		nullable = true 
	)
	private String aa.emails;
	@Column (
		name = "aa.GRUPO",
		nullable = true 
	)
	private String aa.grupo;
	@Column (
		name = "ab.IDXSSOMENUITEM",
		nullable = true 
	)
	private Long ab.idxssomenuitem;
	@Column (
		name = "a.KEYPROCESS",
		nullable = false 
	)
	private String a.keyprocess;
	@Column (
		name = "a.PROCESO",
		nullable = false 
	)
	private String a.proceso;
	private boolean updatable; 

	public void setAaEmailsbcc(
		String var
	) {
		this.aa.emailsbcc=var; 
	}
	
	public String getAaEmailsbcc() {
		return this.aa.emailsbcc; 
	}
	
	public void setAaEmailscc(
		String var
	) {
		this.aa.emailscc=var; 
	}
	
	public String getAaEmailscc() {
		return this.aa.emailscc; 
	}
	
	public void setAaEmails(
		String var
	) {
		this.aa.emails=var; 
	}
	
	public String getAaEmails() {
		return this.aa.emails; 
	}
	
	public void setAaGrupo(
		String var
	) {
		this.aa.grupo=var; 
	}
	
	public String getAaGrupo() {
		return this.aa.grupo; 
	}
	
	public void setAbIdxssomenuitem(
		Long var
	) {
		this.ab.idxssomenuitem=var; 
	}
	
	public Long getAbIdxssomenuitem() {
		return this.ab.idxssomenuitem; 
	}
	
	public void setAKeyprocess(
		String var
	) {
		this.a.keyprocess=var; 
	}
	
	public String getAKeyprocess() {
		return this.a.keyprocess; 
	}
	
	public void setAProceso(
		String var
	) {
		this.a.proceso=var; 
	}
	
	public String getAProceso() {
		return this.a.proceso; 
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