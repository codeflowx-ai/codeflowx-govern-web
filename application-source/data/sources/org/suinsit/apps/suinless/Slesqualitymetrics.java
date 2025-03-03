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
import org.suinsit.apps.suinless.Slesmodel;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESQUALITYMETRICS" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESQUALITYMETRICS",
	labelMonitor = "QUALITY_METRICS",
	pk = "idxslesqualitymetrics" 
)
public class Slesqualitymetrics implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesqualitymetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesqualitymetrics;
	@NotNull
	@NotBlank
	@Column (
		name = "metricdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp metricdate;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "reliability",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal reliability;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "accuracy",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal accuracy;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "consistency",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal consistency;
	@Column (
		name = "qualitytrends",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String qualitytrends;
	@Column (
		name = "detailedmetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String detailedmetrics;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel; 

	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	} 

}