package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESADVANCEDPREDICTION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESADVANCEDPREDICTION",
	labelMonitor = "ADVANCED_PREDICTION",
	pk = "idxslesadvancedprediction" 
)
public class ComSuinsitAppsSuinlessSlesadvancedprediction implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesadvancedprediction",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesadvancedprediction;
	@NotNull
	@NotBlank
	@Column (
		name = "predictiondate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp predictiondate;
	@Column (
		name = "modelensemble",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String modelensemble;
	@Column (
		name = "featureimportance",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String featureimportance;
	@Column (
		name = "uncertaintyestimation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String uncertaintyestimation;
	@Column (
		name = "adaptivelearning",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String adaptivelearning;
	private boolean updatable; 

}