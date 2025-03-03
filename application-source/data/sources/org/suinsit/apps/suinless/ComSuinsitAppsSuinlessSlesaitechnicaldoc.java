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
	name = "SLESAITECHNICALDOC" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAITECHNICALDOC",
	labelMonitor = "AI_TECHNICAL_DOC",
	pk = "idxslesaitechnicaldoc" 
)
public class ComSuinsitAppsSuinlessSlesaitechnicaldoc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesaitechnicaldoc",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesaitechnicaldoc;
	@NotNull
	@NotBlank
	@Column (
		name = "documentdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp documentdate;
	@Column (
		name = "systemdescription",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String systemdescription;
	@Column (
		name = "designspecifications",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String designspecifications;
	@Column (
		name = "developmentmethodology",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String developmentmethodology;
	@Column (
		name = "trainingmethodologies",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String trainingmethodologies;
	@Column (
		name = "validationresults",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String validationresults;
	private boolean updatable; 

}