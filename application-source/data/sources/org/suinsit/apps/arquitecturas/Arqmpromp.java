package org.suinsit.apps.arquitecturas;

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
import org.suinsit.apps.arquitecturas.Arqmexecpromp;
import org.suinsit.apps.arquitecturas.Arqrarqprompt;
import org.suinsit.apps.arquitecturas.Arqrprompflow;
import org.suinsit.apps.arquitecturas.Arqtipoprompt;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQMPROMP" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQMPROMP",
	labelMonitor = "NOMBRE",
	pk = "idxarqmpromp" 
)
public class Arqmpromp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "active",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean active;
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String description;
	@Column (
		name = "example",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String example;
	@Id
	@Column (
		name = "idxarqmpromp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqmpromp;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Column (
		name = "prompt",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String prompt;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQTIPOPROMPT0",
		referencedColumnName = "IDXARQTIPOPROMPT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqtipoprompt idarqtipoprompt;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL0",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLPROVIDER0",
		referencedColumnName = "IDXSLPROVIDER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slprovider idslprovider;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQMEXECPROMP0",
		referencedColumnName = "IDXARQMEXECPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmexecpromp idarqmexecpromp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmpromp" 
	)
	private List<Arqrarqprompt> subarqrarqprompt;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmpromp" 
	)
	private List<Arqrprompflow> subarqrprompflow; 

	public Arqtipoprompt getIdarqtipoprompt() {
		if(this.idarqtipoprompt==null)this.idarqtipoprompt=new org.suinsit.apps.arquitecturas.Arqtipoprompt();
		  return this.idarqtipoprompt; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Arqmexecpromp getIdarqmexecpromp() {
		if(this.idarqmexecpromp==null)this.idarqmexecpromp=new org.suinsit.apps.arquitecturas.Arqmexecpromp();
		  return this.idarqmexecpromp; 
	}
	
	public List<Arqrarqprompt> getSubarqrarqprompt() {
		if(this.subarqrarqprompt==null)this.subarqrarqprompt=new ArrayList<>(0);
		  return this.subarqrarqprompt; 
	}
	
	public List<Arqrprompflow> getSubarqrprompflow() {
		if(this.subarqrprompflow==null)this.subarqrprompflow=new ArrayList<>(0);
		  return this.subarqrprompflow; 
	} 

}