package org.suinsit.apps.admin;

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
import org.suinsit.apps.admin.Aplicacion;
import org.suinsit.apps.admin.Ssomenu;
import org.suinsit.apps.admin.Ssomenuitem;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOMENU" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOMENU",
	labelMonitor = "MENU",
	pk = "idxssomenu" 
)
public class Ssomenu implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dashboardpage",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dashboardpage;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "icono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String icono;
	@Id
	@Column (
		name = "idxssomenu",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssomenu;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "menu",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String menu;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "title",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String title;
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
		name = "IDPARENT0",
		referencedColumnName = "IDXSSOMENU",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssomenu idparent;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idparent" 
	)
	private List<Ssomenu> subssomenu;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idssomenu" 
	)
	private List<Ssomenuitem> subssomenuitem; 

	public Aplicacion getIdssoaplicacion() {
		if(this.idssoaplicacion==null)this.idssoaplicacion=new org.suinsit.apps.admin.Aplicacion();
		  return this.idssoaplicacion; 
	}
	
	public Ssomenu getIdparent() {
		if(this.idparent==null)this.idparent=new org.suinsit.apps.admin.Ssomenu();
		  return this.idparent; 
	}
	
	public List<Ssomenu> getSubssomenu() {
		if(this.subssomenu==null)this.subssomenu=new ArrayList<>(0);
		  return this.subssomenu; 
	}
	
	public List<Ssomenuitem> getSubssomenuitem() {
		if(this.subssomenuitem==null)this.subssomenuitem=new ArrayList<>(0);
		  return this.subssomenuitem; 
	} 

}