package org.suinsit.apps.citaprev;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Mcanalreunion;
import org.suinsit.apps.citaprev.Citmemploy;
import org.suinsit.apps.citaprev.Citmestado;
import org.suinsit.apps.citaprev.Citmservice;
import org.suinsit.apps.crm.Crmcontacto;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.crm.Crmllamada;
import org.suinsit.apps.crm.Crmoportunidad;
import org.suinsit.apps.crm.Crmroportagenda;
import org.suinsit.apps.crm.Crmtipocita;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITMAGENDA" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITMAGENDA",
	labelMonitor = "asunto",
	pk = "idxcrmagenda" 
)
public class Citmagenda implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "motivocancel",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String motivocancel;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "asunto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String asunto;
	@Column (
		name = "avisaren",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp avisaren;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Column (
		name = "begindate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp begindate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cliente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cliente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "contentcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String contentcolor;
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
		max = 16 
	)
	@Column (
		name = "duracion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal duracion;
	@Column (
		name = "enddate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp enddate;
	@Column (
		name = "fecstart",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecstart;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "headercolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String headercolor;
	@Id
	@Column (
		name = "idxcrmagenda",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmagenda;
	@Column (
		name = "locked",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean locked;
	@Column (
		name = "nota",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String nota;
	@Column (
		name = "notificar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean notificar;
	@Column (
		name = "privada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean privada;
	@Column (
		name = "recordatorio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean recordatorio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "reunioncon",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String reunioncon;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMTIPOCITA0",
		referencedColumnName = "IDXCRMTIPOCITA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtipocita idcrmtipocita;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMCANALREUNION0",
		referencedColumnName = "IDXMCANALREUNION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mcanalreunion idmcanalreunion;
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
		name = "IDCRMOPORTUNIDAD0",
		referencedColumnName = "IDXOPORTUNIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmoportunidad idcrmoportunidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMLLAMADA0",
		referencedColumnName = "IDXCRMLLAMADA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmllamada idcrmllamada;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCITMEMPLOY0",
		referencedColumnName = "IDXCITMEMPLOY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmemploy idcitmemploy;
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
		name = "IDCITMSERVICE0",
		referencedColumnName = "IDXCITMSERVICE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmservice idcitmservice;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCITMESTADO0",
		referencedColumnName = "IDXCITMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmestado idcitmestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmagenda" 
	)
	private List<Crmroportagenda> subcrmroportagenda; 

	public Crmtipocita getIdcrmtipocita() {
		if(this.idcrmtipocita==null)this.idcrmtipocita=new org.suinsit.apps.crm.Crmtipocita();
		  return this.idcrmtipocita; 
	}
	
	public Mcanalreunion getIdmcanalreunion() {
		if(this.idmcanalreunion==null)this.idmcanalreunion=new org.suinsit.apps.admin.Mcanalreunion();
		  return this.idmcanalreunion; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Crmoportunidad getIdcrmoportunidad() {
		if(this.idcrmoportunidad==null)this.idcrmoportunidad=new org.suinsit.apps.crm.Crmoportunidad();
		  return this.idcrmoportunidad; 
	}
	
	public Crmllamada getIdcrmllamada() {
		if(this.idcrmllamada==null)this.idcrmllamada=new org.suinsit.apps.crm.Crmllamada();
		  return this.idcrmllamada; 
	}
	
	public Citmemploy getIdcitmemploy() {
		if(this.idcitmemploy==null)this.idcitmemploy=new org.suinsit.apps.citaprev.Citmemploy();
		  return this.idcitmemploy; 
	}
	
	public Crmcontacto getIdcrmcontacto() {
		if(this.idcrmcontacto==null)this.idcrmcontacto=new org.suinsit.apps.crm.Crmcontacto();
		  return this.idcrmcontacto; 
	}
	
	public Citmservice getIdcitmservice() {
		if(this.idcitmservice==null)this.idcitmservice=new org.suinsit.apps.citaprev.Citmservice();
		  return this.idcitmservice; 
	}
	
	public Citmestado getIdcitmestado() {
		if(this.idcitmestado==null)this.idcitmestado=new org.suinsit.apps.citaprev.Citmestado();
		  return this.idcitmestado; 
	}
	
	public List<Crmroportagenda> getSubcrmroportagenda() {
		if(this.subcrmroportagenda==null)this.subcrmroportagenda=new ArrayList<>(0);
		  return this.subcrmroportagenda; 
	} 

}