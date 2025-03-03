package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
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
import org.suinsit.apps.suinless.Slemtypeflow;
import org.suinsit.apps.suinless.Slerprompflow;
import org.suinsit.apps.suinless.Slerpromtmodel;
import org.suinsit.apps.suinless.Slervar;
import org.suinsit.apps.suinless.Slestadopromp;
import org.suinsit.apps.suinless.Slmragdocument;
import org.suinsit.apps.suinless.Slragdatabase;
import org.suinsit.apps.suinless.Slrprojetpromt;
import org.suinsit.apps.suinless.Slrprompsecure;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESPROMP" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESPROMP",
	labelMonitor = "nombre",
	pk = "idxslespromp" 
)
public class Slespromp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "promptflow",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean promptflow;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 25 
	)
	@Column (
		name = "abreviatura",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String abreviatura;
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
		name = "clazzexect",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clazzexect;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clazzorigen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clazzorigen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String couseralta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cousermodif;
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
		name = "example",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String example;
	@NotNull
	@NotBlank
	@Column (
		name = "fecalta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecalta;
	@Id
	@Column (
		name = "idxslespromp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslespromp;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
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
		name = "showmessage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean showmessage;
	@Column (
		name = "showobject",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean showobject;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLRAGDATABASE0",
		referencedColumnName = "IDXSLRAGDATABASE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slragdatabase idslragdatabase;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLMRAGDOCUMENT0",
		referencedColumnName = "IDXSLMRAGDOCUMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmragdocument idslmragdocument;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESTADOPROMP0",
		referencedColumnName = "IDXSLESTADOPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slestadopromp idslestadopromp;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLEMFAMILIAPROMP0",
		referencedColumnName = "IDXSLEMFAMILIAPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slemfamiliapromp idslemfamiliapromp;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESUBFAMILIA0",
		referencedColumnName = "IDXSLEMFAMILIAPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slemfamiliapromp idslesubfamilia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLEMTYPEFLOW0",
		referencedColumnName = "IDXSLEMTYPEFLOW",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slemtypeflow idslemtypeflow;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslespromp" 
	)
	private List<Slerprompflow> subslerprompflow;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslespromp" 
	)
	private List<Slerpromtmodel> subslerpromtmodel;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslespromp" 
	)
	private List<Slervar> subslervar;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslespromp" 
	)
	private List<Slrprojetpromt> subslrprojetpromt;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslespromp" 
	)
	private List<Slrprompsecure> subslrprompsecure; 

	public Slragdatabase getIdslragdatabase() {
		if(this.idslragdatabase==null)this.idslragdatabase=new org.suinsit.apps.suinless.Slragdatabase();
		  return this.idslragdatabase; 
	}
	
	public Slmragdocument getIdslmragdocument() {
		if(this.idslmragdocument==null)this.idslmragdocument=new org.suinsit.apps.suinless.Slmragdocument();
		  return this.idslmragdocument; 
	}
	
	public Slestadopromp getIdslestadopromp() {
		if(this.idslestadopromp==null)this.idslestadopromp=new org.suinsit.apps.suinless.Slestadopromp();
		  return this.idslestadopromp; 
	}
	
	public Slemfamiliapromp getIdslemfamiliapromp() {
		if(this.idslemfamiliapromp==null)this.idslemfamiliapromp=new org.suinsit.apps.suinless.Slemfamiliapromp();
		  return this.idslemfamiliapromp; 
	}
	
	public Slemfamiliapromp getIdslesubfamilia() {
		if(this.idslesubfamilia==null)this.idslesubfamilia=new org.suinsit.apps.suinless.Slemfamiliapromp();
		  return this.idslesubfamilia; 
	}
	
	public Slemtypeflow getIdslemtypeflow() {
		if(this.idslemtypeflow==null)this.idslemtypeflow=new org.suinsit.apps.suinless.Slemtypeflow();
		  return this.idslemtypeflow; 
	}
	
	public List<Slerprompflow> getSubslerprompflow() {
		if(this.subslerprompflow==null)this.subslerprompflow=new ArrayList<>(0);
		  return this.subslerprompflow; 
	}
	
	public List<Slerpromtmodel> getSubslerpromtmodel() {
		if(this.subslerpromtmodel==null)this.subslerpromtmodel=new ArrayList<>(0);
		  return this.subslerpromtmodel; 
	}
	
	public List<Slervar> getSubslervar() {
		if(this.subslervar==null)this.subslervar=new ArrayList<>(0);
		  return this.subslervar; 
	}
	
	public List<Slrprojetpromt> getSubslrprojetpromt() {
		if(this.subslrprojetpromt==null)this.subslrprojetpromt=new ArrayList<>(0);
		  return this.subslrprojetpromt; 
	}
	
	public List<Slrprompsecure> getSubslrprompsecure() {
		if(this.subslrprompsecure==null)this.subslrprompsecure=new ArrayList<>(0);
		  return this.subslrprompsecure; 
	} 

}