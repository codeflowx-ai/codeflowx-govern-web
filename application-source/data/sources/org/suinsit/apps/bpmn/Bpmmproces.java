package org.suinsit.apps.bpmn;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.admin.Aplicacion;
import org.suinsit.apps.admin.Mmodulo;
import org.suinsit.apps.admin.Ssomenuitem;
import org.suinsit.apps.bpmn.Bpmcategoria;
import org.suinsit.apps.bpmn.Bpmnemail;
import org.suinsit.apps.bpmn.Bpmroles;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "BPMMPROCES" 
)
@Entidad (
	namespace = "bpmn",
	type = "TABLE",
	name = "BPMMPROCES",
	labelMonitor = "",
	pk = "idxbpmmproces" 
)
public class Bpmmproces implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "definicion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object definicion;
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
		name = "idxbpmmproces",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxbpmmproces;
	@Column (
		name = "image",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object image;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "keyprocess",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Key Process",
		type = "VARCHAR" 
	)
	private String keyprocess;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proceso",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proceso;
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
		name = "IDSSOAPLICACION0",
		referencedColumnName = "IDXAPLICACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Aplicacion idssoaplicacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMMODULO0",
		referencedColumnName = "IDXMMODULO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mmodulo idmmodulo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDBPMCATEGORIA0",
		referencedColumnName = "IDXBPMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Bpmcategoria idbpmcategoria;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idbpmmproces" 
	)
	private List<Bpmroles> subbpmroles;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idbpmmproces" 
	)
	private List<Ssomenuitem> subssomenuitem;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idbpmmproces" 
	)
	private List<Bpmnemail> subbpmnemail; 

	public Aplicacion getIdssoaplicacion() {
		if(this.idssoaplicacion==null)this.idssoaplicacion=new org.suinsit.apps.admin.Aplicacion();
		  return this.idssoaplicacion; 
	}
	
	public Mmodulo getIdmmodulo() {
		if(this.idmmodulo==null)this.idmmodulo=new org.suinsit.apps.admin.Mmodulo();
		  return this.idmmodulo; 
	}
	
	public Bpmcategoria getIdbpmcategoria() {
		if(this.idbpmcategoria==null)this.idbpmcategoria=new org.suinsit.apps.bpmn.Bpmcategoria();
		  return this.idbpmcategoria; 
	}
	
	public List<Bpmroles> getSubbpmroles() {
		if(this.subbpmroles==null)this.subbpmroles=new ArrayList<>(0);
		  return this.subbpmroles; 
	}
	
	public List<Ssomenuitem> getSubssomenuitem() {
		if(this.subssomenuitem==null)this.subssomenuitem=new ArrayList<>(0);
		  return this.subssomenuitem; 
	}
	
	public List<Bpmnemail> getSubbpmnemail() {
		if(this.subbpmnemail==null)this.subbpmnemail=new ArrayList<>(0);
		  return this.subbpmnemail; 
	} 

}