package org.suinsit.apps.rag;

import java.io.Serializable;
import java.lang.Long;
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
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.rag.Ragaudit;
import org.suinsit.apps.rag.Ragconversacion;
import org.suinsit.apps.rag.Ragdatasource;
import org.suinsit.apps.rag.Ragdocumento;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RAGPROYECTO" 
)
@Entidad (
	namespace = "rag",
	type = "TABLE",
	name = "RAGPROYECTO",
	labelMonitor = "RAGPROYECTO",
	pk = "idxragproject" 
)
public class Ragproyecto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "active",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String active;
	@Column (
		name = "configdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String configdata;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "VARCHAR" 
	)
	private String description;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "estado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "LIST_STRING" 
	)
	private List estado;
	@Column (
		name = "fechacreacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechacreacion;
	@Column (
		name = "fechamodificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DATE" 
	)
	private Date fechamodificacion;
	@Id
	@Column (
		name = "idxragproject",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxragproject;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "projectcode",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String projectcode;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "projectname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String projectname;
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
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idragproyecto" 
	)
	private List<Ragaudit> subragaudit;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idragproyecto" 
	)
	private List<Ragconversacion> subragconversacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idragproyecto" 
	)
	private List<Ragdatasource> subragdatasource;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxragproyecto" 
	)
	private List<Ragdocumento> subragdocumento; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public List<Ragaudit> getSubragaudit() {
		if(this.subragaudit==null)this.subragaudit=new ArrayList<>(0);
		  return this.subragaudit; 
	}
	
	public List<Ragconversacion> getSubragconversacion() {
		if(this.subragconversacion==null)this.subragconversacion=new ArrayList<>(0);
		  return this.subragconversacion; 
	}
	
	public List<Ragdatasource> getSubragdatasource() {
		if(this.subragdatasource==null)this.subragdatasource=new ArrayList<>(0);
		  return this.subragdatasource; 
	}
	
	public List<Ragdocumento> getSubragdocumento() {
		if(this.subragdocumento==null)this.subragdocumento=new ArrayList<>(0);
		  return this.subragdocumento; 
	} 

}