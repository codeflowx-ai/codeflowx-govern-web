package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.myalm.Almapimodel;
import org.suinsit.apps.myalm.Almsdkintegration;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMTIPOSERVICIO" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMTIPOSERVICIO",
	labelMonitor = "TIPO",
	pk = "idxalmtiposervicio" 
)
public class Almtiposervicio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 10 
	)
	@Column (
		name = "acronimo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String acronimo;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activo;
	@Column (
		name = "api",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean api;
	@Id
	@Column (
		name = "idxalmtiposervicio",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmtiposervicio;
	@Column (
		name = "informacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String informacion;
	@Column (
		name = "sdk",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sdk;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmtiposervicio" 
	)
	private List<Almapimodel> subalmapimodel;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmtiposervicio" 
	)
	private List<Almsdkintegration> subalmsdkintegration; 

	public List<Almapimodel> getSubalmapimodel() {
		if(this.subalmapimodel==null)this.subalmapimodel=new ArrayList<>(0);
		  return this.subalmapimodel; 
	}
	
	public List<Almsdkintegration> getSubalmsdkintegration() {
		if(this.subalmsdkintegration==null)this.subalmsdkintegration=new ArrayList<>(0);
		  return this.subalmsdkintegration; 
	} 

}