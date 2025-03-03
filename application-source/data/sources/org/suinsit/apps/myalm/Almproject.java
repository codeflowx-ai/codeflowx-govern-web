package org.suinsit.apps.myalm;

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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.arquitecturas.Arqmarchitecture;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.atlas.Atldomain;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.myalm.Almdatabase;
import org.suinsit.apps.myalm.Almgeneracion;
import org.suinsit.apps.myalm.Almimportbbdd;
import org.suinsit.apps.myalm.Almimportcopy;
import org.suinsit.apps.myalm.Almimportopenapi;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.myalm.Almrclusters;
import org.suinsit.apps.myalm.Almrcomp;
import org.suinsit.apps.myalm.Almrdeploys;
import org.suinsit.apps.myalm.Almrprojenv;
import org.suinsit.apps.myalm.Almrprojscm;
import org.suinsit.apps.myalm.Almrprojusers;
import org.suinsit.apps.myalm.Almrpromod;
import org.suinsit.apps.myalm.Almrtokens;
import org.suinsit.apps.pmo.Pmomproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMPROJECT" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMPROJECT",
	labelMonitor = "PROYECTOALM",
	pk = "idxalmproject" 
)
public class Almproject implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "acronimo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String acronimo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "almcode",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "ALMPROJECT_ALMCODE",
		prefix = "ALM",
		mask = "00000000",
		addYear = true 
	)
	private String almcode;
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
		name = "clone",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean clone;
	@Size (
		min = 0,
		max = 1000 
	)
	@Column (
		name = "descbreve",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descbreve;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dominio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dominio;
	@Id
	@Column (
		name = "idxalmproject",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmproject;
	@Column (
		name = "lekaproj",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean lekaproj;
	@Column (
		name = "lekaproject",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean lekaproject;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = false 
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
		name = "passbasedatos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String passbasedatos;
	@Column (
		name = "produccion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date produccion;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proyectoalm",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proyectoalm;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subdomino",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String subdomino;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urldatabase",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urldatabase;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usuariodatabase",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usuariodatabase;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "uuid",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "UUID",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String uuid;
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
		name = "IDPMOMPROJECT0",
		referencedColumnName = "IDXPMOMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomproject idpmomproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQMARCHITECTURE0",
		referencedColumnName = "IDXARQMARCHITECTURE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmarchitecture idarqmarchitecture;
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
		name = "IDATLDOMAIN0",
		referencedColumnName = "IDXATLDOMAIN",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atldomain idatldomain;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almgeneracion> subalmgeneracion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almimportbbdd> subalmimportbbdd;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almimportcopy> subalmimportcopy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almimportopenapi> subalmimportopenapi;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almproject> subalmproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrclusters> subalmrclusters;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrcomp> subalmrcomp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrdeploys> subalmrdeploys;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrprojenv> subalmrprojenv;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrprojscm> subalmrprojscm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrprojusers> subalmrprojusers;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrpromod> subalmrpromod;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmproject" 
	)
	private List<Almrtokens> subalmrtokens; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Arqmarchitecture getIdarqmarchitecture() {
		if(this.idarqmarchitecture==null)this.idarqmarchitecture=new org.suinsit.apps.arquitecturas.Arqmarchitecture();
		  return this.idarqmarchitecture; 
	}
	
	public Arqsolucion getIdarqsolucion() {
		if(this.idarqsolucion==null)this.idarqsolucion=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idarqsolucion; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Almdatabase getIdalmdatabase() {
		if(this.idalmdatabase==null)this.idalmdatabase=new org.suinsit.apps.myalm.Almdatabase();
		  return this.idalmdatabase; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Atldomain getIdatldomain() {
		if(this.idatldomain==null)this.idatldomain=new org.suinsit.apps.atlas.Atldomain();
		  return this.idatldomain; 
	}
	
	public List<Almgeneracion> getSubalmgeneracion() {
		if(this.subalmgeneracion==null)this.subalmgeneracion=new ArrayList<>(0);
		  return this.subalmgeneracion; 
	}
	
	public List<Almimportbbdd> getSubalmimportbbdd() {
		if(this.subalmimportbbdd==null)this.subalmimportbbdd=new ArrayList<>(0);
		  return this.subalmimportbbdd; 
	}
	
	public List<Almimportcopy> getSubalmimportcopy() {
		if(this.subalmimportcopy==null)this.subalmimportcopy=new ArrayList<>(0);
		  return this.subalmimportcopy; 
	}
	
	public List<Almimportopenapi> getSubalmimportopenapi() {
		if(this.subalmimportopenapi==null)this.subalmimportopenapi=new ArrayList<>(0);
		  return this.subalmimportopenapi; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	}
	
	public List<Almrclusters> getSubalmrclusters() {
		if(this.subalmrclusters==null)this.subalmrclusters=new ArrayList<>(0);
		  return this.subalmrclusters; 
	}
	
	public List<Almrcomp> getSubalmrcomp() {
		if(this.subalmrcomp==null)this.subalmrcomp=new ArrayList<>(0);
		  return this.subalmrcomp; 
	}
	
	public List<Almrdeploys> getSubalmrdeploys() {
		if(this.subalmrdeploys==null)this.subalmrdeploys=new ArrayList<>(0);
		  return this.subalmrdeploys; 
	}
	
	public List<Almrprojenv> getSubalmrprojenv() {
		if(this.subalmrprojenv==null)this.subalmrprojenv=new ArrayList<>(0);
		  return this.subalmrprojenv; 
	}
	
	public List<Almrprojscm> getSubalmrprojscm() {
		if(this.subalmrprojscm==null)this.subalmrprojscm=new ArrayList<>(0);
		  return this.subalmrprojscm; 
	}
	
	public List<Almrprojusers> getSubalmrprojusers() {
		if(this.subalmrprojusers==null)this.subalmrprojusers=new ArrayList<>(0);
		  return this.subalmrprojusers; 
	}
	
	public List<Almrpromod> getSubalmrpromod() {
		if(this.subalmrpromod==null)this.subalmrpromod=new ArrayList<>(0);
		  return this.subalmrpromod; 
	}
	
	public List<Almrtokens> getSubalmrtokens() {
		if(this.subalmrtokens==null)this.subalmrtokens=new ArrayList<>(0);
		  return this.subalmrtokens; 
	} 

}