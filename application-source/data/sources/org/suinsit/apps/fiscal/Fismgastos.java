package org.suinsit.apps.fiscal;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.fiscal.Fismfpago;
import org.suinsit.apps.fiscal.Fismtipogto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FISMGASTOS" 
)
@Entidad (
	namespace = "fiscal",
	type = "TABLE",
	name = "FISMGASTOS",
	labelMonitor = "FACTURA",
	pk = "idxfismgastos" 
)
public class Fismgastos implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ticket",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ticket;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cifproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cifproveedor;
	@Column (
		name = "documento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object documento;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "factura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String factura;
	@Column (
		name = "fechafact",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechafact;
	@Id
	@Column (
		name = "idxfismgastos",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfismgastos;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importeiva",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importeiva;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "imprecargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal imprecargo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impretencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impretencion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentiva",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentiva;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proveedor;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "recequiv",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal recequiv;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "retencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal retencion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "subtotal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal subtotal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "total",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal total;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",1T,2T,3T,4T" 
		},
		message = "solamente admite lo valores: ,1T,2T,3T,4T" 
	)
	@Column (
		name = "trimestre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String trimestre;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFISMTIPOGTO0",
		referencedColumnName = "IDXFISMTIPOGTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Fismtipogto idfismtipogto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFISMFPAGO0",
		referencedColumnName = "IDXFISMFPAGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Fismfpago idfismfpago; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Fismtipogto getIdfismtipogto() {
		if(this.idfismtipogto==null)this.idfismtipogto=new org.suinsit.apps.fiscal.Fismtipogto();
		  return this.idfismtipogto; 
	}
	
	public Fismfpago getIdfismfpago() {
		if(this.idfismfpago==null)this.idfismfpago=new org.suinsit.apps.fiscal.Fismfpago();
		  return this.idfismfpago; 
	} 

}