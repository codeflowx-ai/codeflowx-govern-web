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
	name = "SLESCHATPRODUCTREC" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATPRODUCTREC",
	labelMonitor = "CHAT_PRODUCT_REC",
	pk = "idxsleschatproductrec" 
)
public class Sleschatproductrec implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatproductrec",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatproductrec;
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
		name = "userpreferences",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String userpreferences;
	@Column (
		name = "recommendeditems",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String recommendeditems;
	@Column (
		name = "contextualdata",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String contextualdata;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "conversionrate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal conversionrate;
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