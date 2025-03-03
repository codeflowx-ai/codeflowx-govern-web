package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Sequence;

@Entity
@Table (
	name = "MSERVICIO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxmservicio" 
)
public class Mservicio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "SERVICIOEXT",
		nullable = true 
	)
	private String servicioext;
	@Column (
		name = "CLASEJAVA",
		nullable = true 
	)
	private String clasejava;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "NOMBRE",
		nullable = false 
	)
	private String nombre;
	@Column (
		name = "REFERENCIA",
		nullable = false 
	)
	@Sequence (
		name = "MSERVICIO_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = false 
	)
	private String referencia;
	@Id
	@Column (
		name = "IDXMSERVICIO",
		nullable = true 
	)
	private Long idxmservicio;
	private boolean updatable; 

	public void setServicioext(
		String var
	) {
		this.servicioext=var; 
	}
	
	public String getServicioext() {
		return this.servicioext; 
	}
	
	public void setClasejava(
		String var
	) {
		this.clasejava=var; 
	}
	
	public String getClasejava() {
		return this.clasejava; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setNombre(
		String var
	) {
		this.nombre=var; 
	}
	
	public String getNombre() {
		return this.nombre; 
	}
	
	public void setReferencia(
		String var
	) {
		this.referencia=var; 
	}
	
	public String getReferencia() {
		return this.referencia; 
	}
	
	public void setIdxmservicio(
		Long var
	) {
		this.idxmservicio=var; 
	}
	
	public Long getIdxmservicio() {
		return this.idxmservicio; 
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