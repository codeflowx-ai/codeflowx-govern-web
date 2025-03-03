package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.crm.Crmetapa;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.marketing.Maktmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMPIPELINE" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMPIPELINE",
	labelMonitor = "PIPELINE",
	pk = "idxcrmpipeline" 
)
public class Crmpipeline implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "franquicias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean franquicias;
	@Column (
		name = "apiweb",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean apiweb;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigouuid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Cod. API",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String codigouuid;
	@Column (
		name = "defecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Principal",
		type = "BOOLEAN" 
	)
	private boolean defecto;
	@Id
	@Column (
		name = "idxcrmpipeline",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmpipeline;
	@Column (
		name = "maxreg",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Max. Registros",
		type = "INTEGER" 
	)
	private Integer maxreg;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",CRMOPORTUNIDAD,CRMTASK" 
		},
		message = "solamente admite lo valores: ,CRMOPORTUNIDAD,CRMTASK" 
	)
	@Column (
		name = "modulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Objecto",
		type = "ENUM_STRING" 
	)
	private String modulo;
	@Column (
		name = "ordenasc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Ordenar ascendentemente",
		type = "BOOLEAN" 
	)
	private boolean ordenasc;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pipeline",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Pipeline",
		type = "VARCHAR" 
	)
	private String pipeline;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpipeline" 
	)
	private List<Crmoportunidad> subcrmoportunidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpipeline" 
	)
	private List<Crmetapa> subcrmetapa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpipeline" 
	)
	private List<Maktmoportunidad> submaktmoportunidad; 

	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	}
	
	public List<Crmetapa> getSubcrmetapa() {
		if(this.subcrmetapa==null)this.subcrmetapa=new ArrayList<>(0);
		  return this.subcrmetapa; 
	}
	
	public List<Maktmoportunidad> getSubmaktmoportunidad() {
		if(this.submaktmoportunidad==null)this.submaktmoportunidad=new ArrayList<>(0);
		  return this.submaktmoportunidad; 
	} 

}