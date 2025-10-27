package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.facturacin.Erpempresa;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPALMACEN" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPALMACEN",
	labelMonitor = "ALMACEN",
	pk = "idxalmacen" 
)
public class Mrpalmacen implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "almacen",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String almacen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "direccion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String direccion;
	@Id
	@Column (
		name = "idxalmacen",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmacen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "poblacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String poblacion;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCOMPANY0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa idcompany;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPAIS0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idmpais;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPROVINCIA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mprovincia idmprovincia; 

	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	} 

}