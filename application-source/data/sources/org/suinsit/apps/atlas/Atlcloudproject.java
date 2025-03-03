package org.suinsit.apps.atlas;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.atlas.Atlkubecloud;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.atlas.Atlproveedor;
import org.suinsit.apps.atlas.Atlstorage;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLCLOUDPROJECT" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLCLOUDPROJECT",
	labelMonitor = "PROYECTO",
	pk = "idxatlproject" 
)
public class Atlcloudproject implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proyecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proyecto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refexterna",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refexterna;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refproject",
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
		name = "ATLCLOUDPROJECT_REFPROJECT",
		prefix = "K8S",
		mask = "0000000000",
		addYear = true 
	)
	private String refproject;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlkubecloud> subatlkubecloud;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlproject" 
	)
	private List<Atlstorage> subatlstorage;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlcloudproject" 
	)
	private List<Atlproject> subatlproject; 

	public Atlproveedor getIdatlproveedor() {
		if(this.idatlproveedor==null)this.idatlproveedor=new org.suinsit.apps.atlas.Atlproveedor();
		  return this.idatlproveedor; 
	}
	
	public List<Atlkubecloud> getSubatlkubecloud() {
		if(this.subatlkubecloud==null)this.subatlkubecloud=new ArrayList<>(0);
		  return this.subatlkubecloud; 
	}
	
	public List<Atlstorage> getSubatlstorage() {
		if(this.subatlstorage==null)this.subatlstorage=new ArrayList<>(0);
		  return this.subatlstorage; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	} 

}