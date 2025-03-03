package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.facturacin.Erpfactcompra;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpfacturae;
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erppresupuesto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPEMPRESA" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPEMPRESA",
	labelMonitor = "nombre",
	pk = "idxerpempresa" 
)
public class Erpempresa implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "retencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal retencion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "recargo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal recargo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cif",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "CIF/IVA",
		type = "VARCHAR" 
	)
	private String cif;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ciudad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Ciudad",
		type = "VARCHAR" 
	)
	private String ciudad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codpostal",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Cod. Postal",
		type = "VARCHAR" 
	)
	private String codpostal;
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
		label = "",
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
		label = "",
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
		name = "direccion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Dirección",
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
		name = "email",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Email",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String email;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "folio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String folio;
	@Id
	@Column (
		name = "idxerpempresa",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpempresa;
	@Column (
		name = "logtipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object logtipo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "movil",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String movil;
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
		label = "Nombre Compañia",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "registro",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String registro;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Residente,Residente UE,Extranjero" 
		},
		message = "solamente admite lo valores: ,Residente,Residente UE,Extranjero" 
	)
	@Column (
		name = "residente",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Residente",
		type = "ENUM_STRING" 
	)
	private String residente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sheet",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String sheet;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sitioweb",
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
	private String sitioweb;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String telefono;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Juridica,Fisica" 
		},
		message = "solamente admite lo valores: ,Juridica,Fisica" 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo de Empresa",
		type = "ENUM_STRING" 
	)
	private String tipo;
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
		max = 100 
	)
	@Column (
		name = "volumen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String volumen;
	private boolean updatable;
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
		mappedBy = "idcompany" 
	)
	private List<Erppresupuesto> suberppresupuesto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcompany" 
	)
	private List<Erpfactcompra> suberpfactcompra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcompany" 
	)
	private List<Erpimpuestos> suberpimpuestos;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcompany" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcompany" 
	)
	private List<Erpfacturae> suberpfacturae; 

	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public List<Erppresupuesto> getSuberppresupuesto() {
		if(this.suberppresupuesto==null)this.suberppresupuesto=new ArrayList<>(0);
		  return this.suberppresupuesto; 
	}
	
	public List<Erpfactcompra> getSuberpfactcompra() {
		if(this.suberpfactcompra==null)this.suberpfactcompra=new ArrayList<>(0);
		  return this.suberpfactcompra; 
	}
	
	public List<Erpimpuestos> getSuberpimpuestos() {
		if(this.suberpimpuestos==null)this.suberpimpuestos=new ArrayList<>(0);
		  return this.suberpimpuestos; 
	}
	
	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erpfacturae> getSuberpfacturae() {
		if(this.suberpfacturae==null)this.suberpfacturae=new ArrayList<>(0);
		  return this.suberpfacturae; 
	} 

}