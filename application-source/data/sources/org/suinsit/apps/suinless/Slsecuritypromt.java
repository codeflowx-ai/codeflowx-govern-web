package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slmrulemoderation;
import org.suinsit.apps.suinless.Slprovider;
import org.suinsit.apps.suinless.Slrprompsecure;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLSECURITYPROMT" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLSECURITYPROMT",
	labelMonitor = "NAME",
	pk = "idxslsecuritypromt" 
)
public class Slsecuritypromt implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "message",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean message;
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
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
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
		name = "auditar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean auditar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clazzeval",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clazzeval;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "describe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String describe;
	@Column (
		name = "filterprompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean filterprompt;
	@Column (
		name = "globalchat",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean globalchat;
	@Column (
		name = "globalrag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean globalrag;
	@Id
	@Column (
		name = "idxslsecuritypromt",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslsecuritypromt;
	@Column (
		name = "mediatic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mediatic;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "name",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String name;
	@Column (
		name = "prompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String prompt;
	@Column (
		name = "publicprompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean publicprompt;
	@Column (
		name = "response",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean response;
	@Column (
		name = "showerror",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean showerror;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLPROVIDER0",
		referencedColumnName = "IDXSLPROVIDER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slprovider idslprovider;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL0",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLMRULEMODERATION0",
		referencedColumnName = "IDXSLMRULEMODERATION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmrulemoderation idslmrulemoderation;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslsecuritypromt" 
	)
	private List<Slrprompsecure> subslrprompsecure; 

	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Slmrulemoderation getIdslmrulemoderation() {
		if(this.idslmrulemoderation==null)this.idslmrulemoderation=new org.suinsit.apps.suinless.Slmrulemoderation();
		  return this.idslmrulemoderation; 
	}
	
	public List<Slrprompsecure> getSubslrprompsecure() {
		if(this.subslrprompsecure==null)this.subslrprompsecure=new ArrayList<>(0);
		  return this.subslrprompsecure; 
	} 

}