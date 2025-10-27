package org.suinsit.apps.partners;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.partners.Partner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PTRRAPEL" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "PTRRAPEL",
	labelMonitor = "",
	pk = "idxptrrapel" 
)
public class Ptrrapel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "hastaimp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal hastaimp;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "desdeimp",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal desdeimp;
	@Column (
		name = "fin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fin;
	@Column (
		name = "inicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date inicio;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percent;
	@Id
	@Column (
		name = "idxptrrapel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxptrrapel;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPARTNER0",
		referencedColumnName = "IDXPARTNER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Partner idpartner; 

	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	} 

}