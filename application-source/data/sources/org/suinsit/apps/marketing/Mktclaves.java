package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.marketing.Mktproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTCLAVES" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTCLAVES",
	labelMonitor = "",
	pk = "idxmktclaves" 
)
public class Mktclaves implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "token",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String token;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "credencial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String credencial;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usuario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usuario;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "redsocial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String redsocial;
	@Id
	@Column (
		name = "idxmktclaves",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktclaves;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPROJECT0",
		referencedColumnName = "IDXMKTPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktproject idmktproject; 

	public Mktproject getIdmktproject() {
		if(this.idmktproject==null)this.idmktproject=new org.suinsit.apps.marketing.Mktproject();
		  return this.idmktproject; 
	} 

}