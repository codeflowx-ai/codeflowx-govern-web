package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.suinless.Slemfamiliapromp;
import org.suinsit.apps.suinless.Slespromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLEMFAMILIAPROMP" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLEMFAMILIAPROMP",
	labelMonitor = "FAMILIA",
	pk = "idxslemfamiliapromp" 
)
public class Slemfamiliapromp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "background",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object background;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "familia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String familia;
	@Id
	@Column (
		name = "idxslemfamiliapromp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslemfamiliapromp;
	@Column (
		name = "infointerna",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infointerna;
	@Column (
		name = "infopublic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infopublic;
	@Column (
		name = "publica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean publica;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLEMPARENT0",
		referencedColumnName = "IDXSLEMFAMILIAPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slemfamiliapromp idslemparent;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslemparent" 
	)
	private List<Slemfamiliapromp> subslemfamiliapromp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslemfamiliapromp" 
	)
	private List<Slespromp> subslespromp; 

	public Slemfamiliapromp getIdslemparent() {
		if(this.idslemparent==null)this.idslemparent=new org.suinsit.apps.suinless.Slemfamiliapromp();
		  return this.idslemparent; 
	}
	
	public List<Slemfamiliapromp> getSubslemfamiliapromp() {
		if(this.subslemfamiliapromp==null)this.subslemfamiliapromp=new ArrayList<>(0);
		  return this.subslemfamiliapromp; 
	}
	
	public List<Slespromp> getSubslespromp() {
		if(this.subslespromp==null)this.subslespromp=new ArrayList<>(0);
		  return this.subslespromp; 
	} 

}