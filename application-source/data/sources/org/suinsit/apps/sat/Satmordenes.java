package org.suinsit.apps.sat;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.sat.Satmequipocli;
import org.suinsit.apps.sat.Satmestado;
import org.suinsit.apps.sat.Satrlineaordenprod;
import org.suinsit.apps.sat.Sattiporden;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SATMORDENES" 
)
@Entidad (
	namespace = "sat",
	type = "TABLE",
	name = "SATMORDENES",
	labelMonitor = "NUMORDEN",
	pk = "idxsatmordenes" 
)
public class Satmordenes implements Serializable { 

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
		max = 300 
	)
	@Column (
		name = "autorizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String autorizacion;
	@Column (
		name = "avisoimprevisto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean avisoimprevisto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codgarantia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codgarantia;
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
		name = "comentcliente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentcliente;
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
	@Column (
		name = "diagnostico",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String diagnostico;
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
	@Column (
		name = "entrada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date entrada;
	@Column (
		name = "fechahoracita",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechahoracita;
	@Column (
		name = "garantia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean garantia;
	@Id
	@Column (
		name = "idxsatmordenes",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsatmordenes;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "impuestos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal impuestos;
	@Column (
		name = "notas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String notas;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "numorden",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "SATMORDENES_NUMORDEN",
		prefix = "",
		mask = "00000000",
		addYear = true 
	)
	private String numorden;
	@Column (
		name = "recoger",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean recoger;
	@Column (
		name = "salida",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date salida;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "subtotal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal subtotal;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "total",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal total;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "vatpercent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal vatpercent;
	@Column (
		name = "visitado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean visitado;
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
		name = "IDSATMEQUIPOCLI0",
		referencedColumnName = "IDXSATMEQUIPOCLI",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Satmequipocli idsatmequipocli;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSERVICIOOFICIAL0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idserviciooficial;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFACTURA0",
		referencedColumnName = "IDXERPFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfactura iderpfactura;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSATTIPORDEN0",
		referencedColumnName = "IDXSATTIPORDEN",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sattiporden idsattiporden;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSATMESTADO0",
		referencedColumnName = "IDXSATMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Satmestado idsatmestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsatmordenes" 
	)
	private List<Satrlineaordenprod> subsatrlineaordenprod; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Satmequipocli getIdsatmequipocli() {
		if(this.idsatmequipocli==null)this.idsatmequipocli=new org.suinsit.apps.sat.Satmequipocli();
		  return this.idsatmequipocli; 
	}
	
	public Crmempresa getIdserviciooficial() {
		if(this.idserviciooficial==null)this.idserviciooficial=new org.suinsit.apps.crm.Crmempresa();
		  return this.idserviciooficial; 
	}
	
	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Sattiporden getIdsattiporden() {
		if(this.idsattiporden==null)this.idsattiporden=new org.suinsit.apps.sat.Sattiporden();
		  return this.idsattiporden; 
	}
	
	public Satmestado getIdsatmestado() {
		if(this.idsatmestado==null)this.idsatmestado=new org.suinsit.apps.sat.Satmestado();
		  return this.idsatmestado; 
	}
	
	public List<Satrlineaordenprod> getSubsatrlineaordenprod() {
		if(this.subsatrlineaordenprod==null)this.subsatrlineaordenprod=new ArrayList<>(0);
		  return this.subsatrlineaordenprod; 
	} 

}