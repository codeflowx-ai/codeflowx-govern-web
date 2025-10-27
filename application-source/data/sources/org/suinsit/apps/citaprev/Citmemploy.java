package org.suinsit.apps.citaprev;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.citaprev.Citmagenda;
import org.suinsit.apps.citaprev.Citrusudialibre;
import org.suinsit.apps.citaprev.Citrusuhorario;
import org.suinsit.apps.citaprev.Citrususervice;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITMEMPLOY" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITMEMPLOY",
	labelMonitor = "nombre",
	pk = "idxcitmemploy" 
)
public class Citmemploy implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "infoprivada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infoprivada;
	@Column (
		name = "infopublica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infopublica;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "costehora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal costehora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal precio;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Id
	@Column (
		name = "idxcitmemploy",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcitmemploy;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcitmemploy" 
	)
	private List<Citrusuhorario> subcitrusuhorario;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcitmemploy" 
	)
	private List<Citrususervice> subcitrususervice;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcitmemploy" 
	)
	private List<Citrusudialibre> subcitrusudialibre;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcitmemploy" 
	)
	private List<Citmagenda> subcitmagenda; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public List<Citrusuhorario> getSubcitrusuhorario() {
		if(this.subcitrusuhorario==null)this.subcitrusuhorario=new ArrayList<>(0);
		  return this.subcitrusuhorario; 
	}
	
	public List<Citrususervice> getSubcitrususervice() {
		if(this.subcitrususervice==null)this.subcitrususervice=new ArrayList<>(0);
		  return this.subcitrususervice; 
	}
	
	public List<Citrusudialibre> getSubcitrusudialibre() {
		if(this.subcitrusudialibre==null)this.subcitrusudialibre=new ArrayList<>(0);
		  return this.subcitrusudialibre; 
	}
	
	public List<Citmagenda> getSubcitmagenda() {
		if(this.subcitmagenda==null)this.subcitmagenda=new ArrayList<>(0);
		  return this.subcitmagenda; 
	} 

}