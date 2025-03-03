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
	name = "SLESADVANCEDBEHAVIOR" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESADVANCEDBEHAVIOR",
	labelMonitor = "ADVANCED_BEHAVIOR",
	pk = "idxslesadvancedbehavior" 
)
public class ComSuinsitAppsSuinlessSlesadvancedbehavior implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesadvancedbehavior",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesadvancedbehavior;
	@NotNull
	@NotBlank
	@Column (
		name = "analysisdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp analysisdate;
	@Column (
		name = "behaviorpatterns",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String behaviorpatterns;
	@Column (
		name = "anomalydetection",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String anomalydetection;
	@Column (
		name = "userjourney",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String userjourney;
	@Column (
		name = "adaptiveresponses",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String adaptiveresponses;
	private boolean updatable; 

}