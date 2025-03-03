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
import org.suinsit.apps.arquitecturas.Arqrsolapp;
import org.suinsit.apps.arquitecturas.Arqrsolarq;
import org.suinsit.apps.arquitecturas.Arqrsolcomp;
import org.suinsit.apps.arquitecturas.Arqrsoldeploy;
import org.suinsit.apps.arquitecturas.Arqrsoludata;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.myalm.Almscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQSOLUCION" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQSOLUCION",
	labelMonitor = "solucion",
	pk = "idxarqsolucion" 
)
public class Arqsolucion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "helmchart",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String helmchart;
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
		name = "codigo",
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
		name = "ARQSOLUCION_CODIGO",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codigo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cteuso",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cteuso;
	@Column (
		name = "descamplia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descamplia;
	@Column (
		name = "descorta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descorta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "folderlocal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String folderlocal;
	@Id
	@Column (
		name = "idxarqsolucion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqsolucion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namespace;
	@Column (
		name = "produccion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date produccion;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "solucion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Solución",
		type = "VARCHAR" 
	)
	private String solucion;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSOLUCIONORG0",
		referencedColumnName = "IDXARQSOLUCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqsolucion idsolucionorg;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqsolucion" 
	)
	private List<Arqrsolapp> subarqrsolapp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqsolucion" 
	)
	private List<Arqrsolarq> subarqrsolarq;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqsolucion" 
	)
	private List<Arqrsolcomp> subarqrsolcomp;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqsolucion" 
	)
	private List<Arqrsoldeploy> subarqrsoldeploy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idarqsolucion" 
	)
	private List<Arqrsoludata> subarqrsoludata;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsolucionorg" 
	)
	private List<Arqsolucion> subarqsolucion; 

	public Almscm getIdalmscm() {
		if(this.idalmscm==null)this.idalmscm=new org.suinsit.apps.myalm.Almscm();
		  return this.idalmscm; 
	}
	
	public Arqsolucion getIdsolucionorg() {
		if(this.idsolucionorg==null)this.idsolucionorg=new org.suinsit.apps.arquitecturas.Arqsolucion();
		  return this.idsolucionorg; 
	}
	
	public List<Arqrsolapp> getSubarqrsolapp() {
		if(this.subarqrsolapp==null)this.subarqrsolapp=new ArrayList<>(0);
		  return this.subarqrsolapp; 
	}
	
	public List<Arqrsolarq> getSubarqrsolarq() {
		if(this.subarqrsolarq==null)this.subarqrsolarq=new ArrayList<>(0);
		  return this.subarqrsolarq; 
	}
	
	public List<Arqrsolcomp> getSubarqrsolcomp() {
		if(this.subarqrsolcomp==null)this.subarqrsolcomp=new ArrayList<>(0);
		  return this.subarqrsolcomp; 
	}
	
	public List<Arqrsoldeploy> getSubarqrsoldeploy() {
		if(this.subarqrsoldeploy==null)this.subarqrsoldeploy=new ArrayList<>(0);
		  return this.subarqrsoldeploy; 
	}
	
	public List<Arqrsoludata> getSubarqrsoludata() {
		if(this.subarqrsoludata==null)this.subarqrsoludata=new ArrayList<>(0);
		  return this.subarqrsoludata; 
	}
	
	public List<Arqsolucion> getSubarqsolucion() {
		if(this.subarqsolucion==null)this.subarqsolucion=new ArrayList<>(0);
		  return this.subarqsolucion; 
	} 

}