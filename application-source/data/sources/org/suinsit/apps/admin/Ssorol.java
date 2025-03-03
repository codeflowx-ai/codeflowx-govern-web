package org.suinsit.apps.admin;

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
import org.suinsit.apps.admin.Ssorolesmenu;
import org.suinsit.apps.admin.Ssorportalrol;
import org.suinsit.apps.admin.Ssoruserol;
import org.suinsit.apps.bpmn.Bpmroles;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOROL" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOROL",
	pk = "idxssorol" 
)
public class Ssorol implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxssorol",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssorol;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "rol",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String rol;
	@Column (
		name = "system",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean system;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssorol" 
	)
	private List<Ssoruserol> subssoruserol;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssorol" 
	)
	private List<Ssorolesmenu> subssorolesmenu;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssorol" 
	)
	private List<Bpmroles> subbpmroles;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssorol" 
	)
	private List<Ssorportalrol> subssorportalrol; 

	public List<Ssoruserol> getSubssoruserol() {
		if(this.subssoruserol==null)this.subssoruserol=new ArrayList<>(0);
		  return this.subssoruserol; 
	}
	
	public List<Ssorolesmenu> getSubssorolesmenu() {
		if(this.subssorolesmenu==null)this.subssorolesmenu=new ArrayList<>(0);
		  return this.subssorolesmenu; 
	}
	
	public List<Bpmroles> getSubbpmroles() {
		if(this.subbpmroles==null)this.subbpmroles=new ArrayList<>(0);
		  return this.subbpmroles; 
	}
	
	public List<Ssorportalrol> getSubssorportalrol() {
		if(this.subssorportalrol==null)this.subssorportalrol=new ArrayList<>(0);
		  return this.subssorportalrol; 
	} 

}