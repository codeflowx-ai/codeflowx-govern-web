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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.atlas.Atlkubecloud;
import org.suinsit.apps.atlas.Atlrproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLNODE" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLNODE",
	labelMonitor = "NODO",
	pk = "idxatlnode" 
)
public class Atlnode implements Serializable { 

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
		name = "disco",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer disco;
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean estado;
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
		name = "idxatlnode",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "LONG" 
	)
	private Long idxatlnode;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importemes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importemes;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "iplocal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String iplocal;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ippublica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ippublica;
	@Column (
		name = "maxpods",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxpods;
	@Column (
		name = "memoria",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer memoria;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nodo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nodo;
	@Column (
		name = "pods",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer pods;
	@Column (
		name = "vcpu",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer vcpu;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlnode" 
	)
	private List<Atlrproject> subatlrproject; 

	public Atlkubecloud getIdatlkubecloud() {
		if(this.idatlkubecloud==null)this.idatlkubecloud=new org.suinsit.apps.atlas.Atlkubecloud();
		  return this.idatlkubecloud; 
	}
	
	public List<Atlrproject> getSubatlrproject() {
		if(this.subatlrproject==null)this.subatlrproject=new ArrayList<>(0);
		  return this.subatlrproject; 
	} 

}