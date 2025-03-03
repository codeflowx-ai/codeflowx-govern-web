package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
	name = "SLESCHATORDERTRACKING" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATORDERTRACKING",
	labelMonitor = "CHAT_ORDER_TRACKING",
	pk = "idxsleschatordertracking" 
)
public class Sleschatordertracking implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatordertracking",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatordertracking;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "orderid",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String orderid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "orderstatus",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String orderstatus;
	@Column (
		name = "trackingdetails",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String trackingdetails;
	@Column (
		name = "customerinteractions",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String customerinteractions;
	@Column (
		name = "notificationssent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String notificationssent;
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