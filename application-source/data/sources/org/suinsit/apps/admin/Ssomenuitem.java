package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.suinsit.apps.admin.Ssorolesmenu;
import org.suinsit.apps.bpmn.Bpmmproces;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOMENUITEM" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOMENUITEM",
	labelMonitor = "MENU",
	pk = "idxssomenuitem" 
)
public class Ssomenuitem implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "dashboard",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean dashboard;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "icono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String icono;
	@Id
	@Column (
		name = "idxssomenuitem",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssomenuitem;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "item",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String item;
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
		name = "page",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String page;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOMENU0",
		referencedColumnName = "IDXSSOMENU",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssomenu idssomenu;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDBPMMPROCES0",
		referencedColumnName = "IDXBPMMPROCES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Bpmmproces idbpmmproces;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssomenuitem" 
	)
	private List<Ssorolesmenu> subssorolesmenu; 

	public Ssomenu getIdssomenu() {
		if(this.idssomenu==null)this.idssomenu=new org.suinsit.apps.admin.Ssomenu();
		  return this.idssomenu; 
	}
	
	public Bpmmproces getIdbpmmproces() {
		if(this.idbpmmproces==null)this.idbpmmproces=new org.suinsit.apps.bpmn.Bpmmproces();
		  return this.idbpmmproces; 
	}
	
	public List<Ssorolesmenu> getSubssorolesmenu() {
		if(this.subssorolesmenu==null)this.subssorolesmenu=new ArrayList<>(0);
		  return this.subssorolesmenu; 
	} 

}