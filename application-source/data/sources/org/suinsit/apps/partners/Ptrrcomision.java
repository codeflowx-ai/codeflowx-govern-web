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
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.partners.Partner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PTRRCOMISION" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "PTRRCOMISION",
	labelMonitor = "",
	pk = "idxptrrcomision" 
)
public class Ptrrcomision implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxptrrcomision",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxptrrcomision;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMPRODUCTO0",
		referencedColumnName = "IDXPROMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promproducto idpromproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMCATEGORIA0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idpromcategoria; 

	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	} 

}