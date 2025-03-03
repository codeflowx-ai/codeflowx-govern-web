package org.suinsit.apps.arquitecturas;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.arquitecturas.Arqappdep;
import org.suinsit.apps.arquitecturas.Arqrapparq;
import org.suinsit.apps.arquitecturas.Arqrsolapp;
import org.suinsit.apps.myalm.Almrpromod;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQAPLICACION" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQAPLICACION",
	labelMonitor = "aplicacion",
	pk = "idxarqaplicacion" 
)
public class Arqaplicacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "activa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activa;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "aplicacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String aplicacion;
	@Column (
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@Column (
		name = "base",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean base;
	@Column (
		name = "descripcioncorta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcioncorta;
	@Column (
		name = "descripcionfunc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcionfunc;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "domant",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String domant;
	@Id
	@Column (
		name = "idxarqaplicacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqaplicacion;
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
	@Column (
		name = "pantallas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer pantallas;
	@Column (
		name = "procesos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer procesos;
	@Column (
		name = "reglas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer reglas;
	@Column (
		name = "tablas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer tablas;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	@Column (
		name = "vistas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer vistas;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqaplicacion" 
	)
	private List<Arqrapparq> subarqrapparq;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idappdep" 
	)
	private List<Arqappdep> subarqappdep;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqaplicacion" 
	)
	private List<Arqrsolapp> subarqrsolapp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqaplicacion" 
	)
	private List<Almrpromod> subalmrpromod; 

	public List<Arqrapparq> getSubarqrapparq() {
		if(this.subarqrapparq==null)this.subarqrapparq=new ArrayList<>(0);
		  return this.subarqrapparq; 
	}
	
	public List<Arqappdep> getSubarqappdep() {
		if(this.subarqappdep==null)this.subarqappdep=new ArrayList<>(0);
		  return this.subarqappdep; 
	}
	
	public List<Arqrsolapp> getSubarqrsolapp() {
		if(this.subarqrsolapp==null)this.subarqrsolapp=new ArrayList<>(0);
		  return this.subarqrsolapp; 
	}
	
	public List<Almrpromod> getSubalmrpromod() {
		if(this.subalmrpromod==null)this.subalmrpromod=new ArrayList<>(0);
		  return this.subalmrpromod; 
	} 

}