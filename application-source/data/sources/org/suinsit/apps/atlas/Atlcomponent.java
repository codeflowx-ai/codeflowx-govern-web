package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.atlas.Atlrcompdeploy;
import org.suinsit.apps.atlas.Atlrcomponver;
import org.suinsit.apps.atlas.Atlrkubecomponent;
import org.suinsit.apps.atlas.Atlrproject;
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLCOMPONENT" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLCOMPONENT",
	labelMonitor = "COMPONENT",
	pk = "idxatlcomponent" 
)
public class Atlcomponent implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "componente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String componente;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "deployment",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String deployment;
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
		name = "idxatlcomponent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlcomponent;
	@Column (
		name = "infraestructure",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean infraestructure;
	@Column (
		name = "memory",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer memory;
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
	@Column (
		name = "storage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer storage;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlcomponent" 
	)
	private List<Atlrcompdeploy> subatlrcompdeploy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlcomponent" 
	)
	private List<Atlrcomponver> subatlrcomponver;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlcomponent" 
	)
	private List<Atlrkubecomponent> subatlrkubecomponent;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlcomponent" 
	)
	private List<Atlrproject> subatlrproject; 

	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public List<Atlrcompdeploy> getSubatlrcompdeploy() {
		if(this.subatlrcompdeploy==null)this.subatlrcompdeploy=new ArrayList<>(0);
		  return this.subatlrcompdeploy; 
	}
	
	public List<Atlrcomponver> getSubatlrcomponver() {
		if(this.subatlrcomponver==null)this.subatlrcomponver=new ArrayList<>(0);
		  return this.subatlrcomponver; 
	}
	
	public List<Atlrkubecomponent> getSubatlrkubecomponent() {
		if(this.subatlrkubecomponent==null)this.subatlrkubecomponent=new ArrayList<>(0);
		  return this.subatlrkubecomponent; 
	}
	
	public List<Atlrproject> getSubatlrproject() {
		if(this.subatlrproject==null)this.subatlrproject=new ArrayList<>(0);
		  return this.subatlrproject; 
	} 

}