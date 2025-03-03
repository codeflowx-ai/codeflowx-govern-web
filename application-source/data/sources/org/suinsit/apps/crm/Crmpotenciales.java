package org.suinsit.apps.crm;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmrpotlista;
import org.suinsit.apps.crm.Crmrpotprod;
import org.suinsit.apps.crm.Crmrpotsegm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMPOTENCIALES" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMPOTENCIALES",
	labelMonitor = "EMPRESA",
	pk = "idxcrmpotenciales" 
)
public class Crmpotenciales implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "notascontacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String notascontacto;
	@Size (
		min = 0,
		max = 3500 
	)
	@Column (
		name = "comentariocall",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String comentariocall;
	@Column (
		name = "ultimatarea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp ultimatarea;
	@Column (
		name = "primernegocio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp primernegocio;
	@Column (
		name = "primercontacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp primercontacto;
	@Size (
		min = 0,
		max = 500 
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
	@Size (
		min = 0,
		max = 3500 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
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
		label = "",
		type = "VARCHAR" 
	)
	private String actividad;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activo;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ciudad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ciudad;
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
	@Column (
		name = "conectado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp conectado;
	@Column (
		name = "contactos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer contactos;
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
	private String correo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dominio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dominio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "empresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String empresa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "facebook",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String facebook;
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
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "CRMPOTENCIALES_IDENTIFICADOR",
		prefix = "",
		mask = "0000000000",
		addYear = true 
	)
	private String identificador;
	@Id
	@Column (
		name = "idxcrmpotenciales",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmpotenciales;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "linkedln",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String linkedln;
	@Column (
		name = "llamada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp llamada;
	@Column (
		name = "llamadas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String llamadas;
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
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",RED.ES,PARTNER,WEB,RRSS,otros,BBDD" 
		},
		message = "solamente admite lo valores: ,RED.ES,PARTNER,WEB,RRSS,otros,BBDD" 
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
		name = "personacontacto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String personacontacto;
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
		name = "propietario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String propietario;
	@Column (
		name = "proximallada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp proximallada;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sector",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String sector;
	@Column (
		name = "sendemails",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sendemails;
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
	@Column (
		name = "ultimaactividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp ultimaactividad;
	@Column (
		name = "ultimallamada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp ultimallamada;
	@Column (
		name = "ultimamodificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp ultimamodificacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlweb",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlweb;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpotenciales" 
	)
	private List<Crmrpotprod> subcrmrpotprod;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpotenciales" 
	)
	private List<Crmrpotsegm> subcrmrpotsegm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmpotenciales" 
	)
	private List<Crmrpotlista> subcrmrpotlista; 

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
	
	public List<Crmrpotprod> getSubcrmrpotprod() {
		if(this.subcrmrpotprod==null)this.subcrmrpotprod=new ArrayList<>(0);
		  return this.subcrmrpotprod; 
	}
	
	public List<Crmrpotsegm> getSubcrmrpotsegm() {
		if(this.subcrmrpotsegm==null)this.subcrmrpotsegm=new ArrayList<>(0);
		  return this.subcrmrpotsegm; 
	}
	
	public List<Crmrpotlista> getSubcrmrpotlista() {
		if(this.subcrmrpotlista==null)this.subcrmrpotlista=new ArrayList<>(0);
		  return this.subcrmrpotlista; 
	} 

}