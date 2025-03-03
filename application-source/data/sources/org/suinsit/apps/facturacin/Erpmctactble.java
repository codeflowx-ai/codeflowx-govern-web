package org.suinsit.apps.facturacin;

import java.io.Serializable;
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
import org.suinsit.apps.facturacin.Erpmgasto;
import org.suinsit.apps.facturacin.Erpmgrupoctactble;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.facturacin.Erptipoctacble;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMCTACTBLE" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMCTACTBLE",
	labelMonitor = "numero",
	pk = "idxerpmctactble" 
)
public class Erpmctactble implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "color",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String color;
	@Id
	@Column (
		name = "idxerpmctactble",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmctactble;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "numero",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal numero;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMGRUPOCTACTBLE0",
		referencedColumnName = "IDXERPMGRUPOCTACTBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmgrupoctactble iderpmgrupoctactble;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPTIPOCTACBLE0",
		referencedColumnName = "IDXERPTIPOCTACBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erptipoctacble iderptipoctacble;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmctactble" 
	)
	private List<Erpmgasto> suberpmgasto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcuentacompras" 
	)
	private List<Erpmproveedor> suberpmproveedor; 

	public Erpmgrupoctactble getIderpmgrupoctactble() {
		if(this.iderpmgrupoctactble==null)this.iderpmgrupoctactble=new org.suinsit.apps.facturacin.Erpmgrupoctactble();
		  return this.iderpmgrupoctactble; 
	}
	
	public Erptipoctacble getIderptipoctacble() {
		if(this.iderptipoctacble==null)this.iderptipoctacble=new org.suinsit.apps.facturacin.Erptipoctacble();
		  return this.iderptipoctacble; 
	}
	
	public List<Erpmgasto> getSuberpmgasto() {
		if(this.suberpmgasto==null)this.suberpmgasto=new ArrayList<>(0);
		  return this.suberpmgasto; 
	}
	
	public List<Erpmproveedor> getSuberpmproveedor() {
		if(this.suberpmproveedor==null)this.suberpmproveedor=new ArrayList<>(0);
		  return this.suberpmproveedor; 
	} 

}