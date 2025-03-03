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
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.partners.Partner;
import org.suinsit.apps.subvenciones.Kitestado;
import org.suinsit.apps.subvenciones.Kitrbonocat;
import org.suinsit.apps.subvenciones.Subsolictudes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITDIGITAL" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITDIGITAL",
	labelMonitor = "BONO",
	pk = "idxkitdigital" 
)
public class Kitdigital implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "bono",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String bono;
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
	@NotNull
	@NotBlank
	@Column (
		name = "concesion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha Concesión",
		type = "DATE" 
	)
	private Date concesion;
	@Column (
		name = "finalizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date finalizacion;
	@Id
	@Column (
		name = "idxkitdigital",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitdigital;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBSOLICTUDES0",
		referencedColumnName = "IDXSUBSOLICTUDES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subsolictudes idsubsolictudes;
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
		name = "IDKITESTADO0",
		referencedColumnName = "IDXKITESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitestado idkitestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitdigital" 
	)
	private List<Kitrbonocat> subkitrbonocat; 

	public Subsolictudes getIdsubsolictudes() {
		if(this.idsubsolictudes==null)this.idsubsolictudes=new org.suinsit.apps.subvenciones.Subsolictudes();
		  return this.idsubsolictudes; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	}
	
	public Kitestado getIdkitestado() {
		if(this.idkitestado==null)this.idkitestado=new org.suinsit.apps.subvenciones.Kitestado();
		  return this.idkitestado; 
	}
	
	public List<Kitrbonocat> getSubkitrbonocat() {
		if(this.subkitrbonocat==null)this.subkitrbonocat=new ArrayList<>(0);
		  return this.subkitrbonocat; 
	} 

}