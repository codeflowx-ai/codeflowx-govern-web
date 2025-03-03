package org.suinsit.apps.atlas;

import java.io.Serializable;
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
import org.suinsit.apps.atlas.Atlbalancer;
import org.suinsit.apps.atlas.Atlcloudproject;
import org.suinsit.apps.atlas.Atlnode;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.atlas.Atlproveedor;
import org.suinsit.apps.atlas.Atlrkubecomponent;
import org.suinsit.apps.atlas.Atlstorage;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.myalm.Almrclusters;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLKUBECLOUD" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLKUBECLOUD",
	labelMonitor = "CLUSTER",
	pk = "idxatlkubecloud" 
)
public class Atlkubecloud implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "actualizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date actualizacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "alias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String alias;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cluster",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cluster;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cteestimado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cteestimado;
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
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Id
	@Column (
		name = "idxatlkubecloud",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlkubecloud;
	@Column (
		name = "kubeconfig",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String kubeconfig;
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
		name = "ATLKUBECLOUD_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "region",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String region;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLPROVEEDOR0",
		referencedColumnName = "IDXATLPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproveedor idatlproveedor;
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
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Atlnode> subatlnode;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Atlbalancer> subatlbalancer;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Atlstorage> subatlstorage;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Atlproject> subatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Almrclusters> subalmrclusters;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlkubecloud" 
	)
	private List<Atlrkubecomponent> subatlrkubecomponent; 

	public Atlproveedor getIdatlproveedor() {
		if(this.idatlproveedor==null)this.idatlproveedor=new org.suinsit.apps.atlas.Atlproveedor();
		  return this.idatlproveedor; 
	}
	
	public Atlcloudproject getIdatlcloudproject() {
		if(this.idatlcloudproject==null)this.idatlcloudproject=new org.suinsit.apps.atlas.Atlcloudproject();
		  return this.idatlcloudproject; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Atlnode> getSubatlnode() {
		if(this.subatlnode==null)this.subatlnode=new ArrayList<>(0);
		  return this.subatlnode; 
	}
	
	public List<Atlbalancer> getSubatlbalancer() {
		if(this.subatlbalancer==null)this.subatlbalancer=new ArrayList<>(0);
		  return this.subatlbalancer; 
	}
	
	public List<Atlstorage> getSubatlstorage() {
		if(this.subatlstorage==null)this.subatlstorage=new ArrayList<>(0);
		  return this.subatlstorage; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	}
	
	public List<Almrclusters> getSubalmrclusters() {
		if(this.subalmrclusters==null)this.subalmrclusters=new ArrayList<>(0);
		  return this.subalmrclusters; 
	}
	
	public List<Atlrkubecomponent> getSubatlrkubecomponent() {
		if(this.subatlrkubecomponent==null)this.subatlrkubecomponent=new ArrayList<>(0);
		  return this.subatlrkubecomponent; 
	} 

}