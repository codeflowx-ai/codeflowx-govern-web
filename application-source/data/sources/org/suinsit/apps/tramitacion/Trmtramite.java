package org.suinsit.apps.tramitacion;

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
import org.suinsit.apps.expedientes.Gexmactuacion;
import org.suinsit.apps.expedientes.Gexrnota;
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.subvenciones.Submorganismo;
import org.suinsit.apps.tramitacion.Trmestado;
import org.suinsit.apps.tramitacion.Trmtipotramite;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "TRMTRAMITE" 
)
@Entidad (
	namespace = "tramitacion",
	type = "TABLE",
	name = "TRMTRAMITE",
	labelMonitor = "CODTRAMITE",
	pk = "idxtrmtramite" 
)
public class Trmtramite implements Serializable { 

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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codtramite",
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
		name = "TRMTRAMITE_CODTRAMITE",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codtramite;
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
		name = "fecsolicitud",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fecsolicitud;
	@Column (
		name = "fintramite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fintramite;
	@Id
	@Column (
		name = "idxtrmtramite",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtrmtramite;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "prevfondos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal prevfondos;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDTRMTIPOTRAMITE0",
		referencedColumnName = "IDXTRMTIPOTRAMITE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Trmtipotramite idtrmtipotramite;
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
		name = "IDSUBMORGANISMO0",
		referencedColumnName = "IDXSUBMORGANISMO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Submorganismo idsubmorganismo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSUTRAMITE0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusutramite;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDTRMESTADO0",
		referencedColumnName = "IDXTRMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Trmestado idtrmestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idtrmtramite" 
	)
	private List<Gexmactuacion> subgexmactuacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idtrmtramite" 
	)
	private List<Gexrnota> subgexrnota; 

	public Trmtipotramite getIdtrmtipotramite() {
		if(this.idtrmtipotramite==null)this.idtrmtipotramite=new org.suinsit.apps.tramitacion.Trmtipotramite();
		  return this.idtrmtipotramite; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Submorganismo getIdsubmorganismo() {
		if(this.idsubmorganismo==null)this.idsubmorganismo=new org.suinsit.apps.subvenciones.Submorganismo();
		  return this.idsubmorganismo; 
	}
	
	public Ssousuario getIdusutramite() {
		if(this.idusutramite==null)this.idusutramite=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusutramite; 
	}
	
	public Trmestado getIdtrmestado() {
		if(this.idtrmestado==null)this.idtrmestado=new org.suinsit.apps.tramitacion.Trmestado();
		  return this.idtrmestado; 
	}
	
	public List<Gexmactuacion> getSubgexmactuacion() {
		if(this.subgexmactuacion==null)this.subgexmactuacion=new ArrayList<>(0);
		  return this.subgexmactuacion; 
	}
	
	public List<Gexrnota> getSubgexrnota() {
		if(this.subgexrnota==null)this.subgexrnota=new ArrayList<>(0);
		  return this.subgexrnota; 
	} 

}