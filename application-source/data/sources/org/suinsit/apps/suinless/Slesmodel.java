package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Sletipomodelo;
import org.suinsit.apps.suinless.Slmcategoria;
import org.suinsit.apps.suinless.Slmlicence;
import org.suinsit.apps.suinless.Slmvendor;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESMODEL" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESMODEL",
	labelMonitor = "ALIAS",
	pk = "idxslesmodel" 
)
public class Slesmodel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "negativetext",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String negativetext;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "conditionimage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String conditionimage;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "controlmode",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String controlmode;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "outpaintingmode",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String outpaintingmode;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "maskprompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String maskprompt;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "maskimage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String maskimage;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean activo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "alias",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String alias;
	@Column (
		name = "audio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean audio;
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
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String description;
	@Column (
		name = "download",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean download;
	@Column (
		name = "examples",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String examples;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "formatdownload",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String formatdownload;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "formatupload",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String formatupload;
	@Column (
		name = "free",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean free;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idioma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String idioma;
	@Id
	@Column (
		name = "idxslesmodel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesmodel;
	@Column (
		name = "image",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean image;
	@Column (
		name = "infohome",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String infohome;
	@Column (
		name = "infopublic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String infopublic;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "items",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal items;
	@Column (
		name = "maxcharacter",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer maxcharacter;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "maxfine",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal maxfine;
	@Column (
		name = "maximage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer maximage;
	@Column (
		name = "maxtokens",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer maxtokens;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "model",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String model;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "modelid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String modelid;
	@Column (
		name = "multiidioma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean multiidioma;
	@Column (
		name = "nummaximg",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer nummaximg;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "priceimage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal priceimage;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "priceinput",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal priceinput;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "priceitems",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal priceitems;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "qualitylst",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String qualitylst;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String refproveedor;
	@Column (
		name = "release",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DATE" 
	)
	private Date release;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "scaling",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String scaling;
	@Column (
		name = "size",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "INTEGER" 
	)
	private Integer size;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sizeslst",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String sizeslst;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "style",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String style;
	@Column (
		name = "subsdcrito",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean subsdcrito;
	@Column (
		name = "text",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean text;
	@Column (
		name = "upload",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean upload;
	@Column (
		name = "usedatabase",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean usedatabase;
	@Column (
		name = "useexamples",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean useexamples;
	@Column (
		name = "usehome",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean usehome;
	@Column (
		name = "userag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean userag;
	@Column (
		name = "video",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean video;
	@Column (
		name = "voice",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean voice;
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
		name = "IDSLETIPOMODELO0",
		referencedColumnName = "IDXSLETIPOMODELO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sletipomodelo idsletipomodelo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLMLICENCE0",
		referencedColumnName = "IDXSLMLICENCE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmlicence idslmlicence;
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
		name = "IDSLMVENDOR0",
		referencedColumnName = "IDXSLMVENDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmvendor idslmvendor; 

	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Sletipomodelo getIdsletipomodelo() {
		if(this.idsletipomodelo==null)this.idsletipomodelo=new org.suinsit.apps.suinless.Sletipomodelo();
		  return this.idsletipomodelo; 
	}
	
	public Slmlicence getIdslmlicence() {
		if(this.idslmlicence==null)this.idslmlicence=new org.suinsit.apps.suinless.Slmlicence();
		  return this.idslmlicence; 
	}
	
	public Slmcategoria getIdslmcategoria() {
		if(this.idslmcategoria==null)this.idslmcategoria=new org.suinsit.apps.suinless.Slmcategoria();
		  return this.idslmcategoria; 
	}
	
	public Slmvendor getIdslmvendor() {
		if(this.idslmvendor==null)this.idslmvendor=new org.suinsit.apps.suinless.Slmvendor();
		  return this.idslmvendor; 
	} 

}