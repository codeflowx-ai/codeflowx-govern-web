package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.facturacin.Erpdesfpago;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erppedido;
import org.suinsit.apps.facturacin.Erppresupuesto;
import org.suinsit.apps.facturacli.Clifactura;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPFORMAPAGO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPFORMAPAGO",
	labelMonitor = "FORMADEPAGO",
	pk = "idxerpformapago" 
)
public class Erpformapago implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "mensual",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mensual;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "desfactura",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String desfactura;
	@Column (
		name = "diasfactura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer diasfactura;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "formadepago",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String formadepago;
	@Id
	@Column (
		name = "idxerpformapago",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "LONG" 
	)
	private Long idxerpformapago;
	@Column (
		name = "numpagos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer numpagos;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "porcentaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal porcentaje;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Erpdesfpago> suberpdesfpago;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Subscripcion> subsubscripcion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Clifactura> subclifactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpformapago" 
	)
	private List<Erppedido> suberppedido; 

	public Erpempresa getIderpempresa() {
		if(this.iderpempresa==null)this.iderpempresa=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.iderpempresa; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erpdesfpago> getSuberpdesfpago() {
		if(this.suberpdesfpago==null)this.suberpdesfpago=new ArrayList<>(0);
		  return this.suberpdesfpago; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Subscripcion> getSubsubscripcion() {
		if(this.subsubscripcion==null)this.subsubscripcion=new ArrayList<>(0);
		  return this.subsubscripcion; 
	}
	
	public List<Clifactura> getSubclifactura() {
		if(this.subclifactura==null)this.subclifactura=new ArrayList<>(0);
		  return this.subclifactura; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	} 

}