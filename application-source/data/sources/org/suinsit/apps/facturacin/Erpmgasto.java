package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmctactble;
import org.suinsit.apps.facturacin.Erpmproveedor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMGASTO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMGASTO",
	labelMonitor = "documento",
	pk = "idxerpmgasto" 
)
public class Erpmgasto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String couseralta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String cousermodif;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "documento",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String documento;
	@NotNull
	@NotBlank
	@Column (
		name = "fecha",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Id
	@Column (
		name = "idxerpmgasto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmgasto;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impuestos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impuestos;
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
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
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
	@Column (
		name = "vencimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date vencimiento;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMCONTACTO0",
		referencedColumnName = "IDXCRMCONTACTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmcontacto idcrmcontacto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMCTACTBLE0",
		referencedColumnName = "IDXERPMCTACTBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmctactble iderpmctactble;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMABNCO0",
		referencedColumnName = "IDXERPMABNCO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmabnco iderpmabnco;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPEMPRESA0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa iderpempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMPROVEEDOR0",
		referencedColumnName = "IDXERPMPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmproveedor iderpmproveedor; 

	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	}
	
	public Erpmctactble getIderpmctactble() {
		if(this.iderpmctactble==null)this.iderpmctactble=new org.suinsit.apps.facturacin.Erpmctactble();
		  return this.iderpmctactble; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	}
	
	public Erpempresa getIderpempresa() {
		if(this.iderpempresa==null)this.iderpempresa=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.iderpempresa; 
	}
	
	public Erpmproveedor getIderpmproveedor() {
		if(this.iderpmproveedor==null)this.iderpmproveedor=new org.suinsit.apps.facturacin.Erpmproveedor();
		  return this.iderpmproveedor; 
	} 

}