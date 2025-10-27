package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.FieldType;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.facturacin.Erplineafactprov;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmctactble;
import org.suinsit.apps.facturacin.Erpmgasto;
import org.suinsit.apps.facturacin.Promproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMPROVEEDOR" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMPROVEEDOR",
	labelMonitor = "proveedor",
	pk = "idxerpmproveedor" 
)
public class Erpmproveedor implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "descuento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal descuento;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombrecomercial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombrecomercial;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "correo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String correo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "website",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.URL 
	)
	private String website;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefonos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String telefonos;
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
		label = "",
		type = "VARCHAR" 
	)
	private String provincia;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "identificador",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String identificador;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "uid",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String uid;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proveedor",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proveedor;
	@Id
	@Column (
		name = "idxerpmproveedor",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmproveedor;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCTAVENTAS0",
		referencedColumnName = "IDXERPMCTACTBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmctactble idctaventas;
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
		name = "IDCUENTACOMPRAS0",
		referencedColumnName = "IDXERPMCTACTBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmctactble idcuentacompras;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmproveedor" 
	)
	private List<Promproducto> subpromproducto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmproveedor" 
	)
	private List<Erpmgasto> suberpmgasto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmproveedor" 
	)
	private List<Erplineafactprov> suberplineafactprov; 

	public Erpmctactble getIdctaventas() {
		if(this.idctaventas==null)this.idctaventas=new org.suinsit.apps.facturacin.Erpmctactble();
		  return this.idctaventas; 
	}
	
	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Erpmctactble getIdcuentacompras() {
		if(this.idcuentacompras==null)this.idcuentacompras=new org.suinsit.apps.facturacin.Erpmctactble();
		  return this.idcuentacompras; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	}
	
	public List<Promproducto> getSubpromproducto() {
		if(this.subpromproducto==null)this.subpromproducto=new ArrayList<>(0);
		  return this.subpromproducto; 
	}
	
	public List<Erpmgasto> getSuberpmgasto() {
		if(this.suberpmgasto==null)this.suberpmgasto=new ArrayList<>(0);
		  return this.suberpmgasto; 
	}
	
	public List<Erplineafactprov> getSuberplineafactprov() {
		if(this.suberplineafactprov==null)this.suberplineafactprov=new ArrayList<>(0);
		  return this.suberplineafactprov; 
	} 

}