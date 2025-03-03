package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import javax.persistence.Column;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "VIEW" 
)
public class Rrhhvemploylogin implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "SENDEMAILS",
		nullable = true 
	)
	private boolean sendemails;
	@Column (
		name = "IDSSOUSUARIO0",
		nullable = true 
	)
	private Long idssousuario0;
	@Column (
		name = "IDCRMEMPRESA0",
		nullable = true 
	)
	private Long idcrmempresa0;
	@Column (
		name = "LOCKPORTAL",
		nullable = true 
	)
	private boolean lockportal;
	@Column (
		name = "IDXRRHEMPLEADO",
		nullable = true 
	)
	private Long idxrrhempleado;
	@Column (
		name = "EMPRESA",
		nullable = false 
	)
	private String empresa;
	@Column (
		name = "CIFNIF",
		nullable = true 
	)
	private String cifnif;
	@Column (
		name = "AVATAR",
		nullable = true 
	)
	private Object avatar;
	@Column (
		name = "APELLIDOS",
		nullable = false 
	)
	private String apellidos;
	@Column (
		name = "CORREO",
		nullable = false 
	)
	private String correo;
	@Column (
		name = "DEPARTAMENTO",
		nullable = true 
	)
	private String departamento;
	@Column (
		name = "CORREO",
		nullable = true 
	)
	private String correo;
	@Column (
		name = "NOMBRE",
		nullable = false 
	)
	private String nombre;
	private boolean updatable; 

	public void setSendemails(
		boolean var
	) {
		this.sendemails=var; 
	}
	
	public boolean isSendemails() {
		return this.sendemails; 
	}
	
	public void setIdssousuario0(
		Long var
	) {
		this.idssousuario0=var; 
	}
	
	public Long getIdssousuario0() {
		return this.idssousuario0; 
	}
	
	public void setIdcrmempresa0(
		Long var
	) {
		this.idcrmempresa0=var; 
	}
	
	public Long getIdcrmempresa0() {
		return this.idcrmempresa0; 
	}
	
	public void setLockportal(
		boolean var
	) {
		this.lockportal=var; 
	}
	
	public boolean isLockportal() {
		return this.lockportal; 
	}
	
	public void setIdxrrhempleado(
		Long var
	) {
		this.idxrrhempleado=var; 
	}
	
	public Long getIdxrrhempleado() {
		return this.idxrrhempleado; 
	}
	
	public void setEmpresa(
		String var
	) {
		this.empresa=var; 
	}
	
	public String getEmpresa() {
		return this.empresa; 
	}
	
	public void setCifnif(
		String var
	) {
		this.cifnif=var; 
	}
	
	public String getCifnif() {
		return this.cifnif; 
	}
	
	public void setAvatar(
		Object var
	) {
		this.avatar=var; 
	}
	
	public Object getAvatar() {
		return this.avatar; 
	}
	
	public void setApellidos(
		String var
	) {
		this.apellidos=var; 
	}
	
	public String getApellidos() {
		return this.apellidos; 
	}
	
	public void setCorreo(
		String var
	) {
		this.correo=var; 
	}
	
	public String getCorreo() {
		return this.correo; 
	}
	
	public void setDepartamento(
		String var
	) {
		this.departamento=var; 
	}
	
	public String getDepartamento() {
		return this.departamento; 
	}
	
	public void setCorreo(
		String var
	) {
		this.correo=var; 
	}
	
	public String getCorreo() {
		return this.correo; 
	}
	
	public void setNombre(
		String var
	) {
		this.nombre=var; 
	}
	
	public String getNombre() {
		return this.nombre; 
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