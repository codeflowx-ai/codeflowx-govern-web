package org.suinsit.apps.sat;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.sat.Satmarca;
import org.suinsit.apps.sat.Satmordenes;
import org.suinsit.apps.sat.Satmtipoequipo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATMEQUIPOCLI" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATMEQUIPOCLI",
	labelMonitor = "REFERENCIA",
	pk = "idxsatmequipocli" 
)
public class Satmequipocli implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String color;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "equipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String equipo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "fabricacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String fabricacion;
	@Column (
		name = "feccompra",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccompra;
	@Id
	@Column (
		name = "idxsatmequipocli",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsatmequipocli;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "modelo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String modelo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "SATMEQUIPOCLI_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "serie",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String serie;
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
		name = "IDSATMARCA0",
		referencedColumnName = "IDXSATMARCA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Satmarca idsatmarca;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSATMTIPOEQUIPO0",
		referencedColumnName = "IDXSATMTIPOEQUIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Satmtipoequipo idsatmtipoequipo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsatmequipocli" 
	)
	private List<Satmordenes> subsatmordenes; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Satmarca getIdsatmarca() {
		if(this.idsatmarca==null)this.idsatmarca=new org.suinsit.apps.sat.Satmarca();
		  return this.idsatmarca; 
	}
	
	public Satmtipoequipo getIdsatmtipoequipo() {
		if(this.idsatmtipoequipo==null)this.idsatmtipoequipo=new org.suinsit.apps.sat.Satmtipoequipo();
		  return this.idsatmtipoequipo; 
	}
	
	public List<Satmordenes> getSubsatmordenes() {
		if(this.subsatmordenes==null)this.subsatmordenes=new ArrayList<>(0);
		  return this.subsatmordenes; 
	} 

}