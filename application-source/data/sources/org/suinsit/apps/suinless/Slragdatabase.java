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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.myalm.Almdatabase;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slespromp;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRAGDATABASE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRAGDATABASE",
	pk = "idxslragdatabase" 
)
public class Slragdatabase implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "systemprompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String systemprompt;
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
	@Id
	@Column (
		name = "idxslragdatabase",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslragdatabase;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "indexname",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String indexname;
	@Column (
		name = "modificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date modificacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "password",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String password;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "username",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String username;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMDATABASE0",
		referencedColumnName = "IDXALMDATABASE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almdatabase idalmdatabase;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMPROJECT0",
		referencedColumnName = "IDXALMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almproject idalmproject;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslragdatabase" 
	)
	private List<Slespromp> subslespromp; 

	public Almdatabase getIdalmdatabase() {
		if(this.idalmdatabase==null)this.idalmdatabase=new org.suinsit.apps.myalm.Almdatabase();
		  return this.idalmdatabase; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public List<Slespromp> getSubslespromp() {
		if(this.subslespromp==null)this.subslespromp=new ArrayList<>(0);
		  return this.subslespromp; 
	} 

}