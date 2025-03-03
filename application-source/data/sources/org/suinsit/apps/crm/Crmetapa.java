package org.suinsit.apps.crm;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.bpmn.Bpmmproces;
import org.suinsit.apps.crm.Crmhistetapas;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmpipeline;
import org.suinsit.apps.marketing.Maktmoportunidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMETAPA" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMETAPA",
	labelMonitor = "ETAPA",
	pk = "idxcrmetapa" 
)
public class Crmetapa implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Column (
		name = "cierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "Cierre de etapa",
		type = "BOOLEAN" 
	)
	private boolean cierre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "coduuid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String coduuid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "color",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "color",
		type = "VARCHAR" 
	)
	private String color;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "etapa",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Etapa",
		type = "VARCHAR" 
	)
	private String etapa;
	@Column (
		name = "ganada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ganada;
	@Id
	@Column (
		name = "idxcrmetapa",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxcrmetapa;
	@Column (
		name = "mostrarmotiv",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mostrarmotiv;
	@NotNull
	@NotBlank
	@Column (
		name = "orden",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = false,
		label = "orden",
		type = "INTEGER" 
	)
	private Integer orden;
	@Column (
		name = "perdida",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean perdida;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "porcentaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Porcentaje (%)",
		type = "DECIMAL" 
	)
	private BigDecimal porcentaje;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMPIPELINE0",
		referencedColumnName = "IDXCRMPIPELINE",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Crmpipeline idcrmpipeline;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDBPMMPROCES0",
		referencedColumnName = "IDXBPMMPROCES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Bpmmproces idbpmmproces;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmetapa" 
	)
	private List<Crmoportunidad> subcrmoportunidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmetapa" 
	)
	private List<Crmhistetapas> subcrmhistetapas;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmetapa" 
	)
	private List<Maktmoportunidad> submaktmoportunidad; 

	public Crmpipeline getIdcrmpipeline() {
		if(this.idcrmpipeline==null)this.idcrmpipeline=new org.suinsit.apps.crm.Crmpipeline();
		  return this.idcrmpipeline; 
	}
	
	public Bpmmproces getIdbpmmproces() {
		if(this.idbpmmproces==null)this.idbpmmproces=new org.suinsit.apps.bpmn.Bpmmproces();
		  return this.idbpmmproces; 
	}
	
	public List<Crmoportunidad> getSubcrmoportunidad() {
		if(this.subcrmoportunidad==null)this.subcrmoportunidad=new ArrayList<>(0);
		  return this.subcrmoportunidad; 
	}
	
	public List<Crmhistetapas> getSubcrmhistetapas() {
		if(this.subcrmhistetapas==null)this.subcrmhistetapas=new ArrayList<>(0);
		  return this.subcrmhistetapas; 
	}
	
	public List<Maktmoportunidad> getSubmaktmoportunidad() {
		if(this.submaktmoportunidad==null)this.submaktmoportunidad=new ArrayList<>(0);
		  return this.submaktmoportunidad; 
	} 

}