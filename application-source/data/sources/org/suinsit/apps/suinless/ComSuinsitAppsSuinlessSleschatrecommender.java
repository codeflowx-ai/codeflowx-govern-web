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
	name = "SLESCHATRECOMMENDER" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATRECOMMENDER",
	labelMonitor = "CHAT_RECOMMENDER",
	pk = "idxsleschatrecommender" 
)
public class ComSuinsitAppsSuinlessSleschatrecommender implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatrecommender",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = false,
		type = "LONG" 
	)
	private Long idxsleschatrecommender;
	@NotNull
	@NotBlank
	@Column (
		name = "recommendationdate",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp recommendationdate;
	@Column (
		name = "contextualdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String contextualdata;
	@Column (
		name = "userpreferences",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String userpreferences;
	@Column (
		name = "recommendations",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String recommendations;
	@Column (
		name = "relevancescores",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "OBJECT" 
	)
	private String relevancescores;
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