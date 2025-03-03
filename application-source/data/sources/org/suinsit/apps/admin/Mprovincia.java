package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmdireccion;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Promalmacen;
import org.suinsit.apps.facturacli.Clicliente;
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.subvenciones.Submorganismo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MPROVINCIA" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MPROVINCIA",
	pk = "idx" 
)
public class Mprovincia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ccaa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String ccaa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codccaa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String codccaa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String codigo;
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
		name = "provincia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String provincia;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Rrhempleado> subrrhempleado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Promalmacen> subpromalmacen;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Submorganismo> subsubmorganismo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Crmempresa> subcrmempresa;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Crmcontacto> subcrmcontacto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Crmdireccion> subcrmdireccion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmprovincia" 
	)
	private List<Clicliente> subclicliente; 

	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	}
	
	public List<Promalmacen> getSubpromalmacen() {
		if(this.subpromalmacen==null)this.subpromalmacen=new ArrayList<>(0);
		  return this.subpromalmacen; 
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