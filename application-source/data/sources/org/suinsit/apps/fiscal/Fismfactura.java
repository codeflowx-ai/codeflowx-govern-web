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
import org.suinsit.apps.fiscal.Fismfcbro;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FISMFACTURA" 
)
@Entidad (
	namespace = "fiscal",
	type = "TABLE",
	name = "FISMFACTURA",
	labelMonitor = "factura",
	pk = "idxfismfactura" 
)
public class Fismfactura implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cliente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cliente;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "factura",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String factura;
	@NotNull
	@NotBlank
	@Column (
		name = "fecfactura",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecfactura;
	@Id
	@Column (
		name = "idxfismfactura",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfismfactura;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impirpf",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impirpf;
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
		name = "importeneto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importeneto;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importerecargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importerecargo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nifid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nifid;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentirpf",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentirpf;
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
		max = 16 
	)
	@Column (
		name = "percentrecargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentrecargo;
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
		name = "IDFISMFCBRO0",
		referencedColumnName = "IDXFISMFCBRO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Fismfcbro idfismfcbro; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Fismfcbro getIdfismfcbro() {
		if(this.idfismfcbro==null)this.idfismfcbro=new org.suinsit.apps.fiscal.Fismfcbro();
		  return this.idfismfcbro; 
	} 

}