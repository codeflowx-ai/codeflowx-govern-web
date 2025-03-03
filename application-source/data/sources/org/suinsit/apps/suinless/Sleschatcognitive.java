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
import org.suinsit.apps.suinless.Sleschatbot;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHATCOGNITIVE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATCOGNITIVE",
	labelMonitor = "CHAT_COGNITIVE",
	pk = "idxsleschatcognitive" 
)
public class Sleschatcognitive implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatcognitive",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatcognitive;
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
		name = "resourceusage",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String resourceusage;
	@Column (
		name = "optimizationstrategies",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String optimizationstrategies;
	@Column (
		name = "loadbalancing",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String loadbalancing;
	@Column (
		name = "performancemetrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String performancemetrics;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDXSLESCHATBOT",
		referencedColumnName = "IDXSLESCHATBOT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sleschatbot idxsleschatbot; 

	public Sleschatbot getIdxsleschatbot() {
		if(this.idxsleschatbot==null)this.idxsleschatbot=new org.suinsit.apps.suinless.Sleschatbot();
		  return this.idxsleschatbot; 
	} 

}