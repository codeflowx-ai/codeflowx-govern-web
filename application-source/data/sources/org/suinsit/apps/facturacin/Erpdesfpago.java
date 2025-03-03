package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.facturacin.Erpformapago;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPDESFPAGO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPDESFPAGO",
	pk = "idxerpdesfpago" 
)
public class Erpdesfpago implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Despues de Factura,del mes siguiente,del mes actual" 
		},
		message = "solamente admite lo valores: ,Despues de Factura,del mes siguiente,del mes actual" 
	)
	@Column (
		name = "vencimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String vencimiento;
	@Column (
		name = "dia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer dia;
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
		label = "Importe Fijo",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
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
		label = "Porcentaje",
		type = "DECIMAL" 
	)
	private BigDecimal percent;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Saldo,Porcentaje,Importe" 
		},
		message = "solamente admite lo valores: ,Saldo,Porcentaje,Importe" 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipo;
	@Id
	@Column (
		name = "idxerpdesfpago",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpdesfpago;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFORMAPAGO0",
		referencedColumnName = "IDXERPFORMAPAGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpformapago iderpformapago; 

	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	} 

}