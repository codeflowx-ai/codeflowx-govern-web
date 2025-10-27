package org.suinsit.apps.arquitecturas;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import org.suinsit.apps.arquitecturas.Arqmarketplace;
import org.suinsit.apps.arquitecturas.Arqrapparq;
import org.suinsit.apps.arquitecturas.Arqrarqbbdd;
import org.suinsit.apps.arquitecturas.Arqrclonearq;
import org.suinsit.apps.arquitecturas.Arqrimportdata;
import org.suinsit.apps.arquitecturas.Arqrsolarq;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.arquitecturas.Arqtemplateview;
import org.suinsit.apps.myalm.Almscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQMARCHITECTURE" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQMARCHITECTURE",
	labelMonitor = "arquitectura",
	pk = "idxarqmarchitecture" 
)
public class Arqmarchitecture implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "autor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String autor;
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
		name = "actualizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date actualizacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "arquitectura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String arquitectura;
	@Column (
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "basepaquete",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String basepaquete;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "code",
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
		name = "ARQMARCHITECTURE_CODE",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String code;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "ctepartner",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal ctepartner;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cteproyecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cteproyecto;
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
		name = "descshort",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descshort;
	@Column (
		name = "genleka",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean genleka;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "handlerclass",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Handler Class",
		type = "VARCHAR" 
	)
	private String handlerclass;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "icono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String icono;
	@Id
	@Column (
		name = "idxarqmarchitecture",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqmarchitecture;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "licencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal licencia;
	@Column (
		name = "multicode",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean multicode;
	@Column (
		name = "opensource",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean opensource;
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
	@Column (
		name = "showdesktop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean showdesktop;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subpath",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Subpath",
		type = "VARCHAR" 
	)
	private String subpath;
	@Column (
		name = "useleka",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean useleka;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMSCM0",
		referencedColumnName = "IDXALMSCM",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almscm idalmscm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqmarketplace> subarqmarketplace;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqrapparq> subarqrapparq;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqrarqbbdd> subarqrarqbbdd;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqrclonearq> subarqrclonearq;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqrimportdata> subarqrimportdata;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqrsolarq> subarqrsolarq;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqsolucion> subarqsolucion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqmarchitecture" 
	)
	private List<Arqtemplateview> subarqtemplateview; 

	public Almscm getIdalmscm() {
		if(this.idalmscm==null)this.idalmscm=new org.suinsit.apps.myalm.Almscm();
		  return this.idalmscm; 
	}
	
	public List<Arqmarketplace> getSubarqmarketplace() {
		if(this.subarqmarketplace==null)this.subarqmarketplace=new ArrayList<>(0);
		  return this.subarqmarketplace; 
	}
	
	public List<Arqrapparq> getSubarqrapparq() {
		if(this.subarqrapparq==null)this.subarqrapparq=new ArrayList<>(0);
		  return this.subarqrapparq; 
	}
	
	public List<Arqrarqbbdd> getSubarqrarqbbdd() {
		if(this.subarqrarqbbdd==null)this.subarqrarqbbdd=new ArrayList<>(0);
		  return this.subarqrarqbbdd; 
	}
	
	public List<Arqrclonearq> getSubarqrclonearq() {
		if(this.subarqrclonearq==null)this.subarqrclonearq=new ArrayList<>(0);
		  return this.subarqrclonearq; 
	}
	
	public List<Arqrimportdata> getSubarqrimportdata() {
		if(this.subarqrimportdata==null)this.subarqrimportdata=new ArrayList<>(0);
		  return this.subarqrimportdata; 
	}
	
	public List<Arqrsolarq> getSubarqrsolarq() {
		if(this.subarqrsolarq==null)this.subarqrsolarq=new ArrayList<>(0);
		  return this.subarqrsolarq; 
	}
	
	public List<Arqsolucion> getSubarqsolucion() {
		if(this.subarqsolucion==null)this.subarqsolucion=new ArrayList<>(0);
		  return this.subarqsolucion; 
	}
	
	public List<Arqtemplateview> getSubarqtemplateview() {
		if(this.subarqtemplateview==null)this.subarqtemplateview=new ArrayList<>(0);
		  return this.subarqtemplateview; 
	} 

}