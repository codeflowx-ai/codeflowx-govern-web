package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mactividad;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.admin.Mtipoempresa;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.marketing.Maktmoportunidad;
import org.suinsit.apps.marketing.Mktcampaing;
import org.suinsit.apps.marketing.Mktmestadopot;
import org.suinsit.apps.marketing.Mktreunion;
import org.suinsit.apps.marketing.Mktrlistcustomer;
import org.suinsit.apps.marketing.Mktroportask;
import org.suinsit.apps.marketing.Mktrpotenciallista;
import org.suinsit.apps.marketing.Mktrpotllamda;
import org.suinsit.apps.marketing.Mktrpotoportunidad;
import org.suinsit.apps.marketing.Mktrpotprod;
import org.suinsit.apps.marketing.Mktrpotsegm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTPOTENCIALES" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTPOTENCIALES",
	labelMonitor = "NOMBREDEEMPRESA",
	pk = "idx" 
)
public class Mktpotenciales implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "empleados",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String empleados;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ventas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ventas;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "actividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String actividad;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "comunidadautonoma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String comunidadautonoma;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "contacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String contacto;
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
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String email;
	@Column (
		name = "fechaultima",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechaultima;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "identificador",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String identificador;
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
		max = 16 
	)
	@Column (
		name = "importeprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importeprev;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "localidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String localidad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "movil1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String movil1;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombredeempresa",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Empresa",
		type = "VARCHAR" 
	)
	private String nombredeempresa;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",BBDD,RED.ES,WEB,OTROS" 
		},
		message = "solamente admite lo valores: ,BBDD,RED.ES,WEB,OTROS" 
	)
	@Column (
		name = "origen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String origen;
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
		type = "VARCHAR" 
	)
	private String sitioweb;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sublocalidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String sublocalidad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subsector",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String subsector;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefono1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String telefono1;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefono2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String telefono2;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",CLIENTE,PARTNER,FACTORIA,PRESCRIPTOR" 
		},
		message = "solamente admite lo valores: ,CLIENTE,PARTNER,FACTORIA,PRESCRIPTOR" 
	)
	@Column (
		name = "tipopotencial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipopotencial;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
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
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "volnegocio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal volnegocio;
	private boolean updatable;
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
		name = "IDMACTIVIDAD0",
		referencedColumnName = "IDXACTIVIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mactividad idmactividad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMTIPOEMPRESA0",
		referencedColumnName = "IDXMTIPOEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mtipoempresa idmtipoempresa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTMESTADOPOT0",
		referencedColumnName = "IDXMKTMESTADOPOT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmestadopot idmktmestadopot;
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
		name = "IDUSERALTA0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario iduseralta;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTCAMPAING0",
		referencedColumnName = "IDXMKTCAMPAING",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktcampaing idmktcampaing;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFRCMFRANCHISE0",
		referencedColumnName = "IDXFRCMFRANCHISE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frcmfranchise idfrcmfranchise;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrpotenciallista> submktrpotenciallista;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrlistcustomer> submktrlistcustomer;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrpotllamda> submktrpotllamda;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrpotsegm> submktrpotsegm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrpotprod> submktrpotprod;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktrpotoportunidad> submktrpotoportunidad;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktroportask> submktroportask;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Mktreunion> submktreunion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktpotenciales" 
	)
	private List<Maktmoportunidad> submaktmoportunidad; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Mactividad getIdmactividad() {
		if(this.idmactividad==null)this.idmactividad=new org.suinsit.apps.admin.Mactividad();
		  return this.idmactividad; 
	}
	
	public Mtipoempresa getIdmtipoempresa() {
		if(this.idmtipoempresa==null)this.idmtipoempresa=new org.suinsit.apps.admin.Mtipoempresa();
		  return this.idmtipoempresa; 
	}
	
	public Mktmestadopot getIdmktmestadopot() {
		if(this.idmktmestadopot==null)this.idmktmestadopot=new org.suinsit.apps.marketing.Mktmestadopot();
		  return this.idmktmestadopot; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIduseralta() {
		if(this.iduseralta==null)this.iduseralta=new org.suinsit.apps.admin.Ssousuario();
		  return this.iduseralta; 
	}
	
	public Mktcampaing getIdmktcampaing() {
		if(this.idmktcampaing==null)this.idmktcampaing=new org.suinsit.apps.marketing.Mktcampaing();
		  return this.idmktcampaing; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Mktrpotenciallista> getSubmktrpotenciallista() {
		if(this.submktrpotenciallista==null)this.submktrpotenciallista=new ArrayList<>(0);
		  return this.submktrpotenciallista; 
	}
	
	public List<Mktrlistcustomer> getSubmktrlistcustomer() {
		if(this.submktrlistcustomer==null)this.submktrlistcustomer=new ArrayList<>(0);
		  return this.submktrlistcustomer; 
	}
	
	public List<Mktrpotllamda> getSubmktrpotllamda() {
		if(this.submktrpotllamda==null)this.submktrpotllamda=new ArrayList<>(0);
		  return this.submktrpotllamda; 
	}
	
	public List<Mktrpotsegm> getSubmktrpotsegm() {
		if(this.submktrpotsegm==null)this.submktrpotsegm=new ArrayList<>(0);
		  return this.submktrpotsegm; 
	}
	
	public List<Mktrpotprod> getSubmktrpotprod() {
		if(this.submktrpotprod==null)this.submktrpotprod=new ArrayList<>(0);
		  return this.submktrpotprod; 
	}
	
	public List<Mktrpotoportunidad> getSubmktrpotoportunidad() {
		if(this.submktrpotoportunidad==null)this.submktrpotoportunidad=new ArrayList<>(0);
		  return this.submktrpotoportunidad; 
	}
	
	public List<Mktroportask> getSubmktroportask() {
		if(this.submktroportask==null)this.submktroportask=new ArrayList<>(0);
		  return this.submktroportask; 
	}
	
	public List<Mktreunion> getSubmktreunion() {
		if(this.submktreunion==null)this.submktreunion=new ArrayList<>(0);
		  return this.submktreunion; 
	}
	
	public List<Maktmoportunidad> getSubmaktmoportunidad() {
		if(this.submaktmoportunidad==null)this.submaktmoportunidad=new ArrayList<>(0);
		  return this.submaktmoportunidad; 
	} 

}