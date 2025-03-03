package org.suinsit.apps.admin;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssorportalapp;
import org.suinsit.apps.admin.Ssorportalrol;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOPORTAL" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOPORTAL",
	labelMonitor = "APLICACION",
	pk = "idxssoportal" 
)
public class Ssoportal implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "aplicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String aplicacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "SSOPORTAL_CODIGO",
		prefix = "",
		mask = "00000",
		addYear = false 
	)
	private String codigo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dashboard",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dashboard;
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
	@Id
	@Column (
		name = "idxssoportal",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssoportal;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namespace;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "portal",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Portal",
		type = "VARCHAR" 
	)
	private String portal;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subdominio",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String subdominio;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoportal" 
	)
	private List<Ssorportalrol> subssorportalrol;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoportal" 
	)
	private List<Ssorportalapp> subssorportalapp; 

	public List<Ssorportalrol> getSubssorportalrol() {
		if(this.subssorportalrol==null)this.subssorportalrol=new ArrayList<>(0);
		  return this.subssorportalrol; 
	}
	
	public List<Ssorportalapp> getSubssorportalapp() {
		if(this.subssorportalapp==null)this.subssorportalapp=new ArrayList<>(0);
		  return this.subssorportalapp; 
	} 

}