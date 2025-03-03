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
import org.suinsit.apps.suinless.Sleschatconversation;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHEMOTIONAL" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHEMOTIONAL",
	labelMonitor = "CHAT_EMOTIONAL",
	pk = "idxsleschemotional" 
)
public class Sleschemotional implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschemotional",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschemotional;
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
		name = "emotionalstate",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String emotionalstate;
	@Column (
		name = "empathyresponses",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String empathyresponses;
	@Column (
		name = "personalitytraits",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String personalitytraits;
	@Column (
		name = "adaptivestrategies",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String adaptivestrategies;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDXSLESCHATCONVERSATION",
		referencedColumnName = "IDXSLESCHATCONVERSATION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sleschatconversation idxsleschatconversation; 

	public Sleschatconversation getIdxsleschatconversation() {
		if(this.idxsleschatconversation==null)this.idxsleschatconversation=new org.suinsit.apps.suinless.Sleschatconversation();
		  return this.idxsleschatconversation; 
	} 

}