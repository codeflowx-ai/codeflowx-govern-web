package org.suinsit.apps.subscripciones;

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
import org.suinsit.apps.atlas.Atlhosting;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.facturacin.Erpformapago;
import org.suinsit.apps.facturacin.Erpimpuestos;
import org.suinsit.apps.facturacin.Erpmestado;
import org.suinsit.apps.subscripciones.Periodicidad;
import org.suinsit.apps.subscripciones.Subrcostes;
import org.suinsit.apps.subscripciones.Subrenovacion;
import org.suinsit.apps.subscripciones.Subrfact;
import org.suinsit.apps.subscripciones.Subrsubscprod;
import org.suinsit.apps.subscripciones.Subshoras;
import org.suinsit.apps.subscripciones.Tiposubscrip;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBSCRIPCION" 
)
@Entidad (
	namespace = "subscripciones",
	type = "TABLE",
	name = "SUBSCRIPCION",
	labelMonitor = "Subscripcion",
	pk = "idxsubscripcion" 
)
public class Subscripcion implements Serializable { 

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
	@Column (
		name = "fin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fin;
	@Id
	@Column (
		name = "idxsubscripcion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubscripcion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
	@Column (
		name = "inicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date inicio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subscripcion",
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
		name = "SUBSCRIPCION_SUBSCRIPCION",
		prefix = "SD",
		mask = "0000000000",
		addYear = true 
	)
	private String subscripcion;
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
		name = "IDERPCOMERCIAL0",
		referencedColumnName = "IDXERPCOMERCIAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpcomercial iderpcomercial;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPERIODICIDAD0",
		referencedColumnName = "IDXPERIODICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Periodicidad idperiodicidad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDTIPOSUBSCRIP0",
		referencedColumnName = "IDXTIPOSUBSCRIP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Tiposubscrip idtiposubscrip;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFORMAPAGO0",
		referencedColumnName = "IDXERPFORMAPAGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpformapago iderpformapago;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPIMPUESTOS0",
		referencedColumnName = "IDXERPIMPUESTOS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpimpuestos iderpimpuestos;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMESTADO0",
		referencedColumnName = "IDXERPMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmestado iderpmestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Subrenovacion> subsubrenovacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Subrfact> subsubrfact;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Subrsubscprod> subsubrsubscprod;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Subrcostes> subsubrcostes;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Subshoras> subsubshoras;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Atlproject> subatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubscripcion" 
	)
	private List<Atlhosting> subatlhosting; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Periodicidad getIdperiodicidad() {
		if(this.idperiodicidad==null)this.idperiodicidad=new org.suinsit.apps.subscripciones.Periodicidad();
		  return this.idperiodicidad; 
	}
	
	public Tiposubscrip getIdtiposubscrip() {
		if(this.idtiposubscrip==null)this.idtiposubscrip=new org.suinsit.apps.subscripciones.Tiposubscrip();
		  return this.idtiposubscrip; 
	}
	
	public Erpformapago getIderpformapago() {
		if(this.iderpformapago==null)this.iderpformapago=new org.suinsit.apps.facturacin.Erpformapago();
		  return this.iderpformapago; 
	}
	
	public Erpimpuestos getIderpimpuestos() {
		if(this.iderpimpuestos==null)this.iderpimpuestos=new org.suinsit.apps.facturacin.Erpimpuestos();
		  return this.iderpimpuestos; 
	}
	
	public Erpmestado getIderpmestado() {
		if(this.iderpmestado==null)this.iderpmestado=new org.suinsit.apps.facturacin.Erpmestado();
		  return this.iderpmestado; 
	}
	
	public List<Subrenovacion> getSubsubrenovacion() {
		if(this.subsubrenovacion==null)this.subsubrenovacion=new ArrayList<>(0);
		  return this.subsubrenovacion; 
	}
	
	public List<Subrfact> getSubsubrfact() {
		if(this.subsubrfact==null)this.subsubrfact=new ArrayList<>(0);
		  return this.subsubrfact; 
	}
	
	public List<Subrsubscprod> getSubsubrsubscprod() {
		if(this.subsubrsubscprod==null)this.subsubrsubscprod=new ArrayList<>(0);
		  return this.subsubrsubscprod; 
	}
	
	public List<Subrcostes> getSubsubrcostes() {
		if(this.subsubrcostes==null)this.subsubrcostes=new ArrayList<>(0);
		  return this.subsubrcostes; 
	}
	
	public List<Subshoras> getSubsubshoras() {
		if(this.subsubshoras==null)this.subsubshoras=new ArrayList<>(0);
		  return this.subsubshoras; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	}
	
	public List<Atlhosting> getSubatlhosting() {
		if(this.subatlhosting==null)this.subatlhosting=new ArrayList<>(0);
		  return this.subatlhosting; 
	} 

}