package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;

@Entity
@Table (
	name = "DOCPLANTILLA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxplantilldoc" 
)
public class Docplantilla implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "DOUMENTO",
		nullable = true 
	)
	private Object doumento;
	@Id
	@Column (
		name = "IDXPLANTILLDOC",
		nullable = true 
	)
	private Long idxplantilldoc;
	@Column (
		name = "PLANTILLA",
		nullable = false 
	)
	private String plantilla;
	private boolean updatable; 

	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setDoumento(
		Object var
	) {
		this.doumento=var; 
	}
	
	public Object getDoumento() {
		return this.doumento; 
	}
	
	public void setIdxplantilldoc(
		Long var
	) {
		this.idxplantilldoc=var; 
	}
	
	public Long getIdxplantilldoc() {
		return this.idxplantilldoc; 
	}
	
	public void setPlantilla(
		String var
	) {
		this.plantilla=var; 
	}
	
	public String getPlantilla() {
		return this.plantilla; 
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