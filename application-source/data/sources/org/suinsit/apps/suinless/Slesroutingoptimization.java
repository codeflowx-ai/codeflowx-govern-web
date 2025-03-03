package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
	name = "SLESROUTINGOPTIMIZATION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESROUTINGOPTIMIZATION",
	labelMonitor = "ROUTING_OPTIMIZATION",
	pk = "idxslesroutingoptimization" 
)
public class Slesroutingoptimization implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesroutingoptimization",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesroutingoptimization;
	@NotNull
	@NotBlank
	@Column (
		name = "optimizationdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp optimizationdate;
	@Column (
		name = "optimizationrules",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String optimizationrules;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "costsavings",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal costsavings;
	@Column (
		name = "performanceimprovements",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String performanceimprovements;
	@Column (
		name = "recommendations",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String recommendations;
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