package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
	name = "SLESAICOMPLIANCE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESAICOMPLIANCE",
	labelMonitor = "AI_COMPLIANCE",
	pk = "idxslesaicompliance" 
)
public class ComSuinsitAppsSuinlessSlesaicompliance implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesaicompliance",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxslesaicompliance;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 50 
	)
	@Column (
		name = "regulationtype",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String regulationtype;
	@Column (
		name = "usagecontext",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String usagecontext;
	@Column (
		name = "risklevel",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String risklevel;
	@Column (
		name = "compliancerequirements",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String compliancerequirements;
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
	@Column (
		name = "documentationrequired",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String documentationrequired;
	private boolean updatable; 

}