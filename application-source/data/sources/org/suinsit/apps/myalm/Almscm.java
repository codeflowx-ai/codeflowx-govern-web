package org.suinsit.apps.myalm;

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
import org.suinsit.apps.arquitecturas.Arqmarchitecture;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.myalm.Almenviroment;
import org.suinsit.apps.myalm.Almgeneracion;
import org.suinsit.apps.myalm.Almrprodscm;
import org.suinsit.apps.myalm.Almrprojscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMSCM" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMSCM",
	labelMonitor = "SCM",
	pk = "idxalmscm" 
)
public class Almscm implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "adduseralm",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean adduseralm;
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
		name = "email",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String email;
	@Id
	@Column (
		name = "idxalmscm",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmscm;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "localpath",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String localpath;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramadevelop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramadevelop;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramafeature",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramafeature;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramahotfix",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramahotfix;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramamaster",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramamaster;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramarelease",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramarelease;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramaroot",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramaroot;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "scm",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String scm;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlrepomaster",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlrepomaster;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "username",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String username;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "userpasskey",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String userpasskey;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "versiontag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String versiontag;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMENVIROMENT0",
		referencedColumnName = "IDXALMENVIROMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almenviroment idalmenviroment;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmscm" 
	)
	private List<Almrprodscm> subalmrprodscm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmscm" 
	)
	private List<Almrprojscm> subalmrprojscm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmscm" 
	)
	private List<Almgeneracion> subalmgeneracion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmscm" 
	)
	private List<Arqmarchitecture> subarqmarchitecture;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmscm" 
	)
	private List<Arqsolucion> subarqsolucion; 

	public Almenviroment getIdalmenviroment() {
		if(this.idalmenviroment==null)this.idalmenviroment=new org.suinsit.apps.myalm.Almenviroment();
		  return this.idalmenviroment; 
	}
	
	public List<Almrprodscm> getSubalmrprodscm() {
		if(this.subalmrprodscm==null)this.subalmrprodscm=new ArrayList<>(0);
		  return this.subalmrprodscm; 
	}
	
	public List<Almrprojscm> getSubalmrprojscm() {
		if(this.subalmrprojscm==null)this.subalmrprojscm=new ArrayList<>(0);
		  return this.subalmrprojscm; 
	}
	
	public List<Almgeneracion> getSubalmgeneracion() {
		if(this.subalmgeneracion==null)this.subalmgeneracion=new ArrayList<>(0);
		  return this.subalmgeneracion; 
	}
	
	public List<Arqmarchitecture> getSubarqmarchitecture() {
		if(this.subarqmarchitecture==null)this.subarqmarchitecture=new ArrayList<>(0);
		  return this.subarqmarchitecture; 
	}
	
	public List<Arqsolucion> getSubarqsolucion() {
		if(this.subarqsolucion==null)this.subarqsolucion=new ArrayList<>(0);
		  return this.subarqsolucion; 
	} 

}