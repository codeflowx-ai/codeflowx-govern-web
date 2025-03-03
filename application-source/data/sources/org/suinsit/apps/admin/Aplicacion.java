package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.admin.Ssomenu;
import org.suinsit.apps.admin.Ssorportalapp;
import org.suinsit.apps.admin.Ssoruserapp;
import org.suinsit.apps.bpmn.Bpmmproces;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOAPLICACION" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOAPLICACION",
	labelMonitor = "APLICACION",
	pk = "idxaplicacion" 
)
public class Aplicacion implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		label = "Aplicacion",
		type = "VARCHAR" 
	)
	private String aplicacion;
	@Column (
		name = "ayuda",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String ayuda;
	@Column (
		name = "contexto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean contexto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dominio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dominio;
	@Column (
		name = "enviacorreo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean enviacorreo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "iconclass",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String iconclass;
	@Id
	@Column (
		name = "idxaplicacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxaplicacion;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "passsmtp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String passsmtp;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "prefijo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String prefijo;
	@Column (
		name = "ptosmtp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer ptosmtp;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "puertosmtp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String puertosmtp;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "smtp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String smtp;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "titulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String titulo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usuariosmtp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usuariosmtp;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoaplicacion" 
	)
	private List<Ssoruserapp> subssoruserapp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoaplicacion" 
	)
	private List<Ssomenu> subssomenu;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoaplicacion" 
	)
	private List<Bpmmproces> subbpmmproces;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssoaplicacion" 
	)
	private List<Ssorportalapp> subssorportalapp; 

	public List<Ssoruserapp> getSubssoruserapp() {
		if(this.subssoruserapp==null)this.subssoruserapp=new ArrayList<>(0);
		  return this.subssoruserapp; 
	}
	
	public List<Ssomenu> getSubssomenu() {
		if(this.subssomenu==null)this.subssomenu=new ArrayList<>(0);
		  return this.subssomenu; 
	}
	
	public List<Bpmmproces> getSubbpmmproces() {
		if(this.subbpmmproces==null)this.subbpmmproces=new ArrayList<>(0);
		  return this.subbpmmproces; 
	}
	
	public List<Ssorportalapp> getSubssorportalapp() {
		if(this.subssorportalapp==null)this.subssorportalapp=new ArrayList<>(0);
		  return this.subssorportalapp; 
	} 

}