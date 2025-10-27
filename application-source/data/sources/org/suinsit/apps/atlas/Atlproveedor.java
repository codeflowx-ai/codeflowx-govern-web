package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.suinsit.apps.atlas.Atlhosting;
import org.suinsit.apps.atlas.Atlkubecloud;
import org.suinsit.apps.atlas.Atlproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLPROVEEDOR" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLPROVEEDOR",
	labelMonitor = "PROVEEDOR",
	pk = "idxatlproveedor" 
)
public class Atlproveedor implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "url",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String url;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "partner",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean partner;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proveedor",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proveedor;
	@Id
	@Column (
		name = "idxatlproveedor",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlproveedor;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproveedor" 
	)
	private List<Atlkubecloud> subatlkubecloud;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproveedor" 
	)
	private List<Atlproject> subatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproveedor" 
	)
	private List<Atlhosting> subatlhosting; 

	public List<Atlkubecloud> getSubatlkubecloud() {
		if(this.subatlkubecloud==null)this.subatlkubecloud=new ArrayList<>(0);
		  return this.subatlkubecloud; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	}
	
	public List<Atlhosting> getSubatlhosting() {
		if(this.subatlhosting==null)this.subatlhosting=new ArrayList<>(0);
		  return this.subatlhosting; 
	} 

}