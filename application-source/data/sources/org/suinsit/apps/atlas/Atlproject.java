package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.atlas.Atlcloudproject;
import org.suinsit.apps.atlas.Atldomain;
import org.suinsit.apps.atlas.Atlestados;
import org.suinsit.apps.atlas.Atlhosting;
import org.suinsit.apps.atlas.Atlkubecloud;
import org.suinsit.apps.atlas.Atlrprojdns;
import org.suinsit.apps.atlas.Atlrproject;
import org.suinsit.apps.atlas.Atlrprojinstall;
import org.suinsit.apps.atlas.Atlstorage;
import org.suinsit.apps.atlas.Atltipoproject;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.myalm.Almproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLPROJECT" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLPROJECT",
	labelMonitor = "PROJECT",
	pk = "idxatlproject" 
)
public class Atlproject implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		max = 16 
	)
	@Column (
		name = "costemes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal costemes;
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
	@Id
	@Column (
		name = "idxatlproject",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlproject;
	@Column (
		name = "maxcustomers",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxcustomers;
	@Column (
		name = "maxuser",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxuser;
	@Column (
		name = "maxuserapp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxuserapp;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "project",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String project;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "ATLPROJECT_REFERENCIA",
		prefix = "SC",
		mask = "0000000000",
		addYear = true 
	)
	private String referencia;
	@Column (
		name = "totcustomers",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer totcustomers;
	@Column (
		name = "totuser",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer totuser;
	@Column (
		name = "usersapp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer usersapp;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLCLOUDPROJECT0",
		referencedColumnName = "IDXATLPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlcloudproject idatlcloudproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPCOMERCIAL0",
		referencedColumnName = "IDXERPCOMERCIAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpcomercial iderpcomercial;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLTIPOPROJECT0",
		referencedColumnName = "IDXATLTIPOPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atltipoproject idatltipoproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLKUBECLOUD0",
		referencedColumnName = "IDXATLKUBECLOUD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlkubecloud idatlkubecloud;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMCATEGORIA0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idpromcategoria;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMPRODUCTO0",
		referencedColumnName = "IDXPROMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promproducto idpromproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLESTADOS0",
		referencedColumnName = "IDXATLESTADOS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlestados idatlestados;
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
		name = "IDARQSOLUCION0",
		referencedColumnName = "IDXARQSOLUCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqsolucion idarqsolucion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atldomain> subatldomain;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlhosting> subatlhosting;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlrprojdns> subatlrprojdns;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlrproject> subatlrproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlrprojinstall> subatlrprojinstall;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlstorage> subatlstorage; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Atlcloudproject getIdatlcloudproject() {
		if(this.idatlcloudproject==null)this.idatlcloudproject=new org.suinsit.apps.atlas.Atlcloudproject();
		  return this.idatlcloudproject; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Atltipoproject getIdatltipoproject() {
		if(this.idatltipoproject==null)this.idatltipoproject=new org.suinsit.apps.atlas.Atltipoproject();
		  return this.idatltipoproject; 
	}
	
	public Atlkubecloud getIdatlkubecloud() {
		if(this.idatlkubecloud==null)this.idatlkubecloud=new org.suinsit.apps.atlas.Atlkubecloud();
		  return this.idatlkubecloud; 
	}
	
	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Atlestados getIdatlestados() {
		if(this.idatlestados==null)this.idatlestados=new org.suinsit.apps.atlas.Atlestados();
		  return this.idatlestados; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	}
	
	public List<Atldomain> getSubatldomain() {
		if(this.subatldomain==null)this.subatldomain=new ArrayList<>(0);
		  return this.subatldomain; 
	}
	
	public List<Atlhosting> getSubatlhosting() {
		if(this.subatlhosting==null)this.subatlhosting=new ArrayList<>(0);
		  return this.subatlhosting; 
	}
	
	public List<Atlrprojdns> getSubatlrprojdns() {
		if(this.subatlrprojdns==null)this.subatlrprojdns=new ArrayList<>(0);
		  return this.subatlrprojdns; 
	}
	
	public List<Atlrproject> getSubatlrproject() {
		if(this.subatlrproject==null)this.subatlrproject=new ArrayList<>(0);
		  return this.subatlrproject; 
	}
	
	public List<Atlrprojinstall> getSubatlrprojinstall() {
		if(this.subatlrprojinstall==null)this.subatlrprojinstall=new ArrayList<>(0);
		  return this.subatlrprojinstall; 
	}
	
	public List<Atlstorage> getSubatlstorage() {
		if(this.subatlstorage==null)this.subatlstorage=new ArrayList<>(0);
		  return this.subatlstorage; 
	} 

}