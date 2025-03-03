package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmetapa;
import org.suinsit.apps.crm.Crmpipeline;
import org.suinsit.apps.crm.Crmtiponegocio;
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MAKTMOPORTUNIDAD" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MAKTMOPORTUNIDAD",
	labelMonitor = "oportunidad",
	pk = "idxmaktmoportunidad" 
)
public class Maktmoportunidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "motcierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String motcierre;
	@Column (
		name = "cerrado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cerrado;
	@Column (
		name = "archivado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean archivado;
	@Column (
		name = "feccierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccierre;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "probabilidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal probabilidad;
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
		max = 16 
	)
	@Column (
		name = "impfinal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impfinal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impprevisto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impprevisto;
	@Column (
		name = "fecprevista",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecprevista;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "oportunidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String oportunidad;
	@Id
	@Column (
		name = "idxmaktmoportunidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmaktmoportunidad;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROPIETARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idpropietario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMETAPA0",
		referencedColumnName = "IDXCRMETAPA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmetapa idcrmetapa;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMPIPELINE0",
		referencedColumnName = "IDXCRMPIPELINE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmpipeline idcrmpipeline;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMTIPONEGOCIO0",
		referencedColumnName = "IDXCRMTIPONEGOCIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmtiponegocio idcrmtiponegocio;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPOTENCIALES0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktpotenciales idmktpotenciales; 

	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Crmetapa getIdcrmetapa() {
		if(this.idcrmetapa==null)this.idcrmetapa=new org.suinsit.apps.crm.Crmetapa();
		  return this.idcrmetapa; 
	}
	
	public Crmpipeline getIdcrmpipeline() {
		if(this.idcrmpipeline==null)this.idcrmpipeline=new org.suinsit.apps.crm.Crmpipeline();
		  return this.idcrmpipeline; 
	}
	
	public Crmtiponegocio getIdcrmtiponegocio() {
		if(this.idcrmtiponegocio==null)this.idcrmtiponegocio=new org.suinsit.apps.crm.Crmtiponegocio();
		  return this.idcrmtiponegocio; 
	}
	
	public Mktpotenciales getIdmktpotenciales() {
		if(this.idmktpotenciales==null)this.idmktpotenciales=new org.suinsit.apps.marketing.Mktpotenciales();
		  return this.idmktpotenciales; 
	} 

}