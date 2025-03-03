package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
import org.enartframework.nocode.annotacion.ValidEnum;
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
	name = "ERPMESTADO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMESTADO",
	labelMonitor = "ESTADO",
	pk = "idxerpmestado" 
)
public class Erpmestado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "remove",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean remove;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark,bg-danger" 
		},
		message = "solamente admite lo valores: ,bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark,bg-danger" 
	)
	@Column (
		name = "bgcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String bgcolor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Estado",
		type = "VARCHAR" 
	)
	private String estado;
	@Column (
		name = "factura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean factura;
	@Id
	@Column (
		name = "idxerpmestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmestado;
	@Column (
		name = "pedido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean pedido;
	@Column (
		name = "presupuesto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean presupuesto;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",CREACION,ENVIO,ANUALADA,CANCELADA,ACEPTADA,PENDIENTE" 
		},
		message = "solamente admite lo valores: ,CREACION,ENVIO,ANUALADA,CANCELADA,ACEPTADA,PENDIENTE" 
	)
	@Column (
		name = "whenaction",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String whenaction;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestado" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestado" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestado" 
	)
	private List<Subscripcion> subsubscripcion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestado" 
	)
	private List<Erppedido> suberppedido;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestado" 
	)
	private List<Clifactura> subclifactura; 

	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Subscripcion> getSubsubscripcion() {
		if(this.subsubscripcion==null)this.subsubscripcion=new ArrayList<>(0);
		  return this.subsubscripcion; 
	}
	
	public List<Erppedido> getSuberppedido() {
		if(this.suberppedido==null)this.suberppedido=new ArrayList<>(0);
		  return this.suberppedido; 
	}
	
	public List<Clifactura> getSubclifactura() {
		if(this.subclifactura==null)this.subclifactura=new ArrayList<>(0);
		  return this.subclifactura; 
	} 

}