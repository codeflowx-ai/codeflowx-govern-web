package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.suinsit.apps.suinless.Sldepartament;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slmcategoria;
import org.suinsit.apps.suinless.Slmragdocument;
import org.suinsit.apps.suinless.Slrtemplatemodels;
import org.suinsit.apps.suinless.Sltemplatemeta;
import org.suinsit.apps.suinless.Sltemplatevar;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLTEMPLATE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLTEMPLATE",
	labelMonitor = "NAME",
	pk = "idxsltemplate" 
)
public class Sltemplate implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "createdat",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp createdat;
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String description;
	@Column (
		name = "enablerag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean enablerag;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "fields",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LIST_STRING" 
	)
	private List fields;
	@Column (
		name = "helpusers",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String helpusers;
	@Id
	@Column (
		name = "idxsltemplate",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsltemplate;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "metadata",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "MAPOBJECT" 
	)
	private Map metadata;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "name",
		nullable = true 
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
		name = "promptemplate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String promptemplate;
	@Size (
		min = 0,
		max = 150 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String status;
	@Size (
		min = 0,
		max = 150 
	)
	@Column (
		name = "type",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "ENUM_STRING" 
	)
	private String type;
	@Column (
		name = "updatedat",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp updatedat;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "variables",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "MAPSTRING" 
	)
	private Map variables;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLDEPARTAMENT0",
		referencedColumnName = "IDXSLDEPARTAMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sldepartament idsldepartament;
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
		name = "IDSLMCATEGORIA0",
		referencedColumnName = "IDXSLMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmcategoria idslmcategoria;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsltemplate" 
	)
	private List<Slrtemplatemodels> subslrtemplatemodels;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsltemplate" 
	)
	private List<Sltemplatevar> subsltemplatevar;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsltemplate" 
	)
	private List<Sltemplatemeta> subsltemplatemeta; 

	public Sldepartament getIdsldepartament() {
		if(this.idsldepartament==null)this.idsldepartament=new org.suinsit.apps.suinless.Sldepartament();
		  return this.idsldepartament; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Slmcategoria getIdslmcategoria() {
		if(this.idslmcategoria==null)this.idslmcategoria=new org.suinsit.apps.suinless.Slmcategoria();
		  return this.idslmcategoria; 
	}
	
	public Slmragdocument getIdslmragdocument() {
		if(this.idslmragdocument==null)this.idslmragdocument=new org.suinsit.apps.suinless.Slmragdocument();
		  return this.idslmragdocument; 
	}
	
	public List<Slrtemplatemodels> getSubslrtemplatemodels() {
		if(this.subslrtemplatemodels==null)this.subslrtemplatemodels=new ArrayList<>(0);
		  return this.subslrtemplatemodels; 
	}
	
	public List<Sltemplatevar> getSubsltemplatevar() {
		if(this.subsltemplatevar==null)this.subsltemplatevar=new ArrayList<>(0);
		  return this.subsltemplatevar; 
	}
	
	public List<Sltemplatemeta> getSubsltemplatemeta() {
		if(this.subsltemplatemeta==null)this.subsltemplatemeta=new ArrayList<>(0);
		  return this.subsltemplatemeta; 
	} 

}