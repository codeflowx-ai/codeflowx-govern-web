package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	name = "SLESMODELCOMPARISON" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESMODELCOMPARISON",
	labelMonitor = "MODEL_COMPARISON",
	pk = "idxslesmodelcomparison" 
)
public class Slesmodelcomparison implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesmodelcomparison",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesmodelcomparison;
	@NotNull
	@NotBlank
	@Column (
		name = "comparisondate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp comparisondate;
	@Column (
		name = "performancecomparison",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String performancecomparison;
	@Column (
		name = "costcomparison",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String costcomparison;
	@Column (
		name = "featurecomparison",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String featurecomparison;
	@Column (
		name = "usecaseanalysis",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String usecaseanalysis;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL1",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel1;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL2",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel2; 

	public Slesmodel getIdslesmodel1() {
		if(this.idslesmodel1==null)this.idslesmodel1=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel1; 
	}
	
	public Slesmodel getIdslesmodel2() {
		if(this.idslesmodel2==null)this.idslesmodel2=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel2; 
	} 

}