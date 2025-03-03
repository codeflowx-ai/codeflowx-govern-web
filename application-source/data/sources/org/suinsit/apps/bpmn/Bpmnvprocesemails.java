package org.suinsit.apps.bpmn;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "VIEW" 
)
public class Bpmnvprocesemails implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Column (
		name = "a.VERSION",
		nullable = true 
	)
	private String a.version;
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
		name = "aa.DESCRIPCION",
		nullable = true 
	)
	private String aa.descripcion;
	@Column (
		name = "aa.EMAILS",
		nullable = true 
	)
	private String aa.emails;
	@Column (
		name = "a.IDXBPMMPROCES",
		nullable = true 
	)
	private Long a.idxbpmmproces;
	@Column (
		name = "aa.GRUPO",
		nullable = true 
	)
	private String aa.grupo;
	private boolean updatable; 

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
	
	public void setAVersion(
		String var
	) {
		this.a.version=var; 
	}
	
	public String getAVersion() {
		return this.a.version; 
	}
	
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
	
	public void setAaDescripcion(
		String var
	) {
		this.aa.descripcion=var; 
	}
	
	public String getAaDescripcion() {
		return this.aa.descripcion; 
	}
	
	public void setAaEmails(
		String var
	) {
		this.aa.emails=var; 
	}
	
	public String getAaEmails() {
		return this.aa.emails; 
	}
	
	public void setAIdxbpmmproces(
		Long var
	) {
		this.a.idxbpmmproces=var; 
	}
	
	public Long getAIdxbpmmproces() {
		return this.a.idxbpmmproces; 
	}
	
	public void setAaGrupo(
		String var
	) {
		this.aa.grupo=var; 
	}
	
	public String getAaGrupo() {
		return this.aa.grupo; 
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