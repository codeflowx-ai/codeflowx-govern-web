package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	name = "SLESAIRISK" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAIRISK",
	labelMonitor = "AI_RISK",
	pk = "idxslesairisk" 
)
public class ComSuinsitAppsSuinlessSlesairisk implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesairisk",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesairisk;
	@NotNull
	@NotBlank
	@Column (
		name = "assessmentdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp assessmentdate;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 50 
	)
	@Column (
		name = "risklevel",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String risklevel;
	@Column (
		name = "riskscore",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal riskscore;
	@Column (
		name = "autonomylevel",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal autonomylevel;
	@Column (
		name = "selflearning",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean selflearning;
	@Column (
		name = "mitigationmeasures",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String mitigationmeasures;
	private boolean updatable; 

}