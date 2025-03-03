package org.suinsit.apps.atlas;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.atlas.Atlproveedor;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.soporte.Sopmticket;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLHOSTING" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLHOSTING",
	labelMonitor = "dominio",
	pk = "idxatlhosting" 
)
public class Atlhosting implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "costeprov",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal costeprov;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
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
	@Column (
		name = "ecommerce",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ecommerce;
	@Id
	@Column (
		name = "idxatlhosting",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlhosting;
	@Column (
		name = "mantenimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mantenimiento;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "password",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String password;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "productoprov",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String productoprov;
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
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "ATLHOSTING_REFERENCIA",
		prefix = "HS",
		mask = "0000000000",
		addYear = true 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refproveedor;
	@Column (
		name = "support",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean support;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urladmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urladmin;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usuarioadmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usuarioadmin;
	@Column (
		name = "vencimiento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date vencimiento;
	@Column (
		name = "webdesing",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean webdesing;
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
		name = "IDPROMCATEGORIA0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idpromcategoria;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMPRODUCTO0",
		referencedColumnName = "IDXPROMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promproducto idpromproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLPROVEEDOR0",
		referencedColumnName = "IDXATLPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproveedor idatlproveedor;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLPROJECT0",
		referencedColumnName = "IDXATLPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproject idatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlhosting" 
	)
	private List<Sopmticket> subsopmticket; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Atlproveedor getIdatlproveedor() {
		if(this.idatlproveedor==null)this.idatlproveedor=new org.suinsit.apps.atlas.Atlproveedor();
		  return this.idatlproveedor; 
	}
	
	public Atlproject getIdatlproject() {
		if(this.idatlproject==null)this.idatlproject=new org.suinsit.apps.atlas.Atlproject();
		  return this.idatlproject; 
	}
	
	public List<Sopmticket> getSubsopmticket() {
		if(this.subsopmticket==null)this.subsopmticket=new ArrayList<>(0);
		  return this.subsopmticket; 
	} 

}