package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "QUERY" 
)
public class Qrhemploylogin implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "DEPARTAMENTO",
		nullable = true 
	)
	private String departamento;
	@Column (
		name = "IDXRRHEMPLEADO",
		nullable = true 
	)
	private Long idxrrhempleado;
	@Column (
		name = "LOCKPORTAL",
		nullable = true 
	)
	private boolean lockportal;
	@Column (
		name = "NOMBRE",
		nullable = false 
	)
	private String nombre;
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
	private boolean updatable; 

	public void setDepartamento(
		String var
	) {
		this.departamento=var; 
	}
	
	public String getDepartamento() {
		return this.departamento; 
	}
	
	public void setIdxrrhempleado(
		Long var
	) {
		this.idxrrhempleado=var; 
	}
	
	public Long getIdxrrhempleado() {
		return this.idxrrhempleado; 
	}
	
	public void setLockportal(
		boolean var
	) {
		this.lockportal=var; 
	}
	
	public boolean isLockportal() {
		return this.lockportal; 
	}
	
	public void setNombre(
		String var
	) {
		this.nombre=var; 
	}
	
	public String getNombre() {
		return this.nombre; 
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
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	} 

}