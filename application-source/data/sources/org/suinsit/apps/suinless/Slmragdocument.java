package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slespromp;
import org.suinsit.apps.suinless.Slprovider;
import org.suinsit.apps.suinless.Slrragdocuments;
import org.suinsit.apps.suinless.Sltemplate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLMRAGDOCUMENT" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLMRAGDOCUMENT",
	labelMonitor = "ASSISTANT",
	pk = "idxslmragdocument" 
)
public class Slmragdocument implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "messagefin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String messagefin;
	@Column (
		name = "multidioma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean multidioma;
	@Column (
		name = "interactive",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean interactive;
	@Column (
		name = "maxconsultas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxconsultas;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "assistant",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String assistant;
	@Column (
		name = "chatbot",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean chatbot;
	@Id
	@Column (
		name = "idxslmragdocument",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslmragdocument;
	@Column (
		name = "modif",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date modif;
	@Column (
		name = "prompters",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String prompters;
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
		name = "IDALMPROJECT0",
		referencedColumnName = "IDXALMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almproject idalmproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmragdocument" 
	)
	private List<Slespromp> subslespromp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmragdocument" 
	)
	private List<Slrragdocuments> subslrragdocuments;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmragdocument" 
	)
	private List<Sltemplate> subsltemplate; 

	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public List<Slespromp> getSubslespromp() {
		if(this.subslespromp==null)this.subslespromp=new ArrayList<>(0);
		  return this.subslespromp; 
	}
	
	public List<Slrragdocuments> getSubslrragdocuments() {
		if(this.subslrragdocuments==null)this.subslrragdocuments=new ArrayList<>(0);
		  return this.subslrragdocuments; 
	}
	
	public List<Sltemplate> getSubsltemplate() {
		if(this.subsltemplate==null)this.subsltemplate=new ArrayList<>(0);
		  return this.subsltemplate; 
	} 

}