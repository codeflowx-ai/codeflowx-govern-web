package org.suinsit.apps.admin;

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
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmdireccion;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpmproveedor;
import org.suinsit.apps.facturacin.Promalmacen;
import org.suinsit.apps.facturacli.Clicliente;
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.subvenciones.Submorganismo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MPAIS" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MPAIS",
	pk = "idx" 
)
public class Mpais implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@NotBlank
	@Column (
		name = "idx",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idx;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "iso2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String iso2;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "iso3",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String iso3;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "name",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String name;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nom",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String nom;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "phonecode",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String phonecode;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Mprovincia> submprovincia;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Rrhempleado> subrrhempleado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Promalmacen> subpromalmacen;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Erpmproveedor> suberpmproveedor;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Submorganismo> subsubmorganismo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Crmempresa> subcrmempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Crmcontacto> subcrmcontacto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Crmdireccion> subcrmdireccion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmpais" 
	)
	private List<Clicliente> subclicliente; 

	public List<Mprovincia> getSubmprovincia() {
		if(this.submprovincia==null)this.submprovincia=new ArrayList<>(0);
		  return this.submprovincia; 
	}
	
	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	}
	
	public List<Promalmacen> getSubpromalmacen() {
		if(this.subpromalmacen==null)this.subpromalmacen=new ArrayList<>(0);
		  return this.subpromalmacen; 
	}
	
	public List<Erpmproveedor> getSuberpmproveedor() {
		if(this.suberpmproveedor==null)this.suberpmproveedor=new ArrayList<>(0);
		  return this.suberpmproveedor; 
	}
	
	public List<Submorganismo> getSubsubmorganismo() {
		if(this.subsubmorganismo==null)this.subsubmorganismo=new ArrayList<>(0);
		  return this.subsubmorganismo; 
	}
	
	public List<Crmempresa> getSubcrmempresa() {
		if(this.subcrmempresa==null)this.subcrmempresa=new ArrayList<>(0);
		  return this.subcrmempresa; 
	}
	
	public List<Crmcontacto> getSubcrmcontacto() {
		if(this.subcrmcontacto==null)this.subcrmcontacto=new ArrayList<>(0);
		  return this.subcrmcontacto; 
	}
	
	public List<Crmdireccion> getSubcrmdireccion() {
		if(this.subcrmdireccion==null)this.subcrmdireccion=new ArrayList<>(0);
		  return this.subcrmdireccion; 
	}
	
	public List<Clicliente> getSubclicliente() {
		if(this.subclicliente==null)this.subclicliente=new ArrayList<>(0);
		  return this.subclicliente; 
	} 

}