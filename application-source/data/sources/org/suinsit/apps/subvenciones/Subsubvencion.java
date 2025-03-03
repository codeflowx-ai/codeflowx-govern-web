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
import org.enartframework.nocode.annotacion.FieldType;
import org.suinsit.apps.subvenciones.Submorganismo;
import org.suinsit.apps.subvenciones.Submtiposub;
import org.suinsit.apps.subvenciones.Subrsubdoc;
import org.suinsit.apps.subvenciones.Subrsubvcont;
import org.suinsit.apps.subvenciones.Subsolictudes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBSUBVENCION" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "SUBSUBVENCION",
	labelMonitor = "SUBVENCION",
	pk = "idxsubsubvencion" 
)
public class Subsubvencion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "comunicado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comunicado;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailtramite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.EMAIL 
	)
	private String emailtramite;
	@Column (
		name = "fechacierre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechacierre;
	@Column (
		name = "fechainicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechainicio;
	@Column (
		name = "fecpublicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecpublicacion;
	@Id
	@Column (
		name = "idxsubsubvencion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubsubvencion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importemaximo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importemaximo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "maxsubvencion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal maxsubvencion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentinteres",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentinteres;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentprestamo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentprestamo;
	@Column (
		name = "requisitos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String requisitos;
	@Column (
		name = "resumen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String resumen;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subvencion",
		nullable = false 
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
	@Column (
		name = "vigente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean vigente;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "webpublicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.URL 
	)
	private String webpublicacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "webtramite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@FieldType (
		type = FieldType.TYPEVALIDATOR.URL 
	)
	private String webtramite;
	private boolean updatable;
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
		name = "IDSUBMTIPOSUB0",
		referencedColumnName = "IDXSUBMTIPOSUB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Submtiposub idsubmtiposub;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsubvencion" 
	)
	private List<Subsolictudes> subsubsolictudes;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsubvencion" 
	)
	private List<Subrsubdoc> subsubrsubdoc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubsubvencion" 
	)
	private List<Subrsubvcont> subsubrsubvcont; 

	public Submorganismo getIdsubmorganismo() {
		if(this.idsubmorganismo==null)this.idsubmorganismo=new org.suinsit.apps.subvenciones.Submorganismo();
		  return this.idsubmorganismo; 
	}
	
	public Submtiposub getIdsubmtiposub() {
		if(this.idsubmtiposub==null)this.idsubmtiposub=new org.suinsit.apps.subvenciones.Submtiposub();
		  return this.idsubmtiposub; 
	}
	
	public List<Subsolictudes> getSubsubsolictudes() {
		if(this.subsubsolictudes==null)this.subsubsolictudes=new ArrayList<>(0);
		  return this.subsubsolictudes; 
	}
	
	public List<Subrsubdoc> getSubsubrsubdoc() {
		if(this.subsubrsubdoc==null)this.subsubrsubdoc=new ArrayList<>(0);
		  return this.subsubrsubdoc; 
	}
	
	public List<Subrsubvcont> getSubsubrsubvcont() {
		if(this.subsubrsubvcont==null)this.subsubrsubvcont=new ArrayList<>(0);
		  return this.subsubrsubvcont; 
	} 

}