package org.suinsit.apps.facturacli;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacli.Clicliente;
import org.suinsit.apps.facturacli.Clifactura;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CLICLIENTE" 
)
@Entidad (
	namespace = "facturacli",
	type = "TABLE",
	name = "CLICLIENTE",
	labelMonitor = "nombre",
	pk = "idxclicliente" 
)
public class Clicliente implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "iban",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String iban;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "apellidos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String apellidos;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codpostal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codpostal;
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
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String direccion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "email",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String email;
	@Id
	@Column (
		name = "idxclicliente",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxclicliente;
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
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nomcomercial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nomcomercial;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Persona fisica,Persona Juridica" 
		},
		message = "solamente admite lo valores: ,Persona fisica,Persona Juridica" 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "vatid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String vatid;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLICLIENTE0",
		referencedColumnName = "IDXCLICLIENTE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Clicliente idclicliente;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idclicliente" 
	)
	private List<Clifactura> subclifactura; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Clicliente getIdclicliente() {
		if(this.idclicliente==null)this.idclicliente=new org.suinsit.apps.facturacli.Clicliente();
		  return this.idclicliente; 
	}
	
	public List<Clifactura> getSubclifactura() {
		if(this.subclifactura==null)this.subclifactura=new ArrayList<>(0);
		  return this.subclifactura; 
	} 

}