package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slesroutingstrategy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESLOADBALANCING" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESLOADBALANCING",
	labelMonitor = "LOAD_BALANCING",
	pk = "idxslesloadbalancing" 
)
public class Slesloadbalancing implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesloadbalancing",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesloadbalancing;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "balancingtype",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String balancingtype;
	@Column (
		name = "weightconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String weightconfig;
	@Column (
		name = "healthchecks",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String healthchecks;
	@Column (
		name = "capacityconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String capacityconfig;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESROUTINGSTRATEGY",
		referencedColumnName = "IDXSLESROUTINGSTRATEGY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesroutingstrategy idslesroutingstrategy; 

	public Slesroutingstrategy getIdslesroutingstrategy() {
		if(this.idslesroutingstrategy==null)this.idslesroutingstrategy=new org.suinsit.apps.suinless.Slesroutingstrategy();
		  return this.idslesroutingstrategy; 
	} 

}