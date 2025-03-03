package org.suinsit.apps.subvenciones;

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
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.partners.Partner;
import org.suinsit.apps.portalemp.Rrhhimputaciones;
import org.suinsit.apps.subvenciones.Kitdigital;
import org.suinsit.apps.subvenciones.Kitrbonocat;
import org.suinsit.apps.subvenciones.Submestado;
import org.suinsit.apps.subvenciones.Subnotificacion;
import org.suinsit.apps.subvenciones.Subrsoldoc;
import org.suinsit.apps.subvenciones.Subsubvencion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBSOLICTUDES" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "SUBSOLICTUDES",
	labelMonitor = "CODSOLICITUD",
	pk = "idxsubsolictudes" 
)
public class Subsolictudes implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codsolicitud",
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
		name = "SUBSOLICTUDES_CODSOLICITUD",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codsolicitud;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "comitercero",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal comitercero;
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
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "fecharesolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecharesolucion;
	@NotNull
	@NotBlank
	@Column (
		name = "fechasolicitud",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "fecha solicitud",
		type = "DATE" 
	)
	private Date fechasolicitud;
	@Id
	@Column (
		name = "idxsubsolictudes",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubsolictudes;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importeconsedido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importeconsedido;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importefijo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importefijo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importesolictado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importesolictado;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentdirect",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentdirect;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentexito",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentexito;
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
		type = "VARCHAR" 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subvencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String subvencion;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBSUBVENCION0",
		referencedColumnName = "IDXSUBSUBVENCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subsubvencion idsubsubvencion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBMESTADO0",
		referencedColumnName = "IDXSUBMESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Submestado idsubmestado;
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
		name = "IDPARTNER0",
		referencedColumnName = "IDXPARTNER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Partner idpartner;
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
		mappedBy = "idsubsolictudes" 
	)
	private List<Subnotificacion> subsubnotificacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsolictudes" 
	)
	private List<Subrsoldoc> subsubrsoldoc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsolictudes" 
	)
	private List<Rrhhimputaciones> subrrhhimputaciones;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsolictudes" 
	)
	private List<Kitdigital> subkitdigital;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsolictudes" 
	)
	private List<Kitrbonocat> subkitrbonocat; 

	public Subsubvencion getIdsubsubvencion() {
		if(this.idsubsubvencion==null)this.idsubsubvencion=new org.suinsit.apps.subvenciones.Subsubvencion();
		  return this.idsubsubvencion; 
	}
	
	public Submestado getIdsubmestado() {
		if(this.idsubmestado==null)this.idsubmestado=new org.suinsit.apps.subvenciones.Submestado();
		  return this.idsubmestado; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Subnotificacion> getSubsubnotificacion() {
		if(this.subsubnotificacion==null)this.subsubnotificacion=new ArrayList<>(0);
		  return this.subsubnotificacion; 
	}
	
	public List<Subrsoldoc> getSubsubrsoldoc() {
		if(this.subsubrsoldoc==null)this.subsubrsoldoc=new ArrayList<>(0);
		  return this.subsubrsoldoc; 
	}
	
	public List<Rrhhimputaciones> getSubrrhhimputaciones() {
		if(this.subrrhhimputaciones==null)this.subrrhhimputaciones=new ArrayList<>(0);
		  return this.subrrhhimputaciones; 
	}
	
	public List<Kitdigital> getSubkitdigital() {
		if(this.subkitdigital==null)this.subkitdigital=new ArrayList<>(0);
		  return this.subkitdigital; 
	}
	
	public List<Kitrbonocat> getSubkitrbonocat() {
		if(this.subkitrbonocat==null)this.subkitrbonocat=new ArrayList<>(0);
		  return this.subkitrbonocat; 
	} 

}