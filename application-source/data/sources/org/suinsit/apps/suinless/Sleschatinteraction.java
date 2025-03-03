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
import org.suinsit.apps.suinless.Sleschatconversation;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHATINTERACTION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATINTERACTION",
	labelMonitor = "CHAT_INTERACTION",
	pk = "idxsleschatinteraction" 
)
public class Sleschatinteraction implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatinteraction",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatinteraction;
	@NotNull
	@NotBlank
	@Column (
		name = "interactiontime",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp interactiontime;
	@Column (
		name = "userquery",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "CLOB" 
	)
	private String userquery;
	@Column (
		name = "botresponse",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "CLOB" 
	)
	private String botresponse;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "sentiment",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal sentiment;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "intentdetected",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String intentdetected;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "confidencescore",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal confidencescore;
	@Column (
		name = "ragcontext",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String ragcontext;
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