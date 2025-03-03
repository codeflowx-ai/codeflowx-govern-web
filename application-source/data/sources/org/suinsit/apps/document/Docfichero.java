package org.suinsit.apps.document;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.asesor4.Notificacion;
import org.suinsit.apps.asesor4.Sacticket;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.document.Doccatalogo;
import org.suinsit.apps.document.Docrlinks;
import org.suinsit.apps.document.Doctipodocum;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "DOCFICHERO" 
)
@Entidad (
	namespace = "document",
	type = "TABLE",
	name = "DOCFICHERO",
	labelMonitor = "NAMEFILE",
	pk = "idxdocfichero" 
)
public class Docfichero implements Serializable { 

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
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
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
		max = 300 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "documento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String documento;
	@Column (
		name = "file",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object file;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "idtupla",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal idtupla;
	@Id
	@Column (
		name = "idxdocfichero",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxdocfichero;
	@Column (
		name = "lectura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp lectura;
	@Column (
		name = "locked",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean locked;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "mes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String mes;
	@Column (
		name = "modificado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp modificado;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "modulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String modulo;
	@Column (
		name = "mostrarportal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mostrarportal;
	@Size (
		min = 0,
		max = 300 
	)
	@Column (
		name = "namefile",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namefile;
	@Size (
		min = 0,
		max = 500 
	)
	@Column (
		name = "path",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String path;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "periodo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String periodo;
	@Column (
		name = "privado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean privado;
	@Column (
		name = "size",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer size;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipomime",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipomime;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
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
		type = "VARCHAR" 
	)
	private String tmmodif;
	@Column (
		name = "year",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer year;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDDOCTIPODOCUM0",
		referencedColumnName = "IDXDOCTIPODOCUM",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Doctipodocum iddoctipodocum;
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
		name = "IDUSUARIOBLOCK0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusuarioblock;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDDOCCATALOGO0",
		referencedColumnName = "IDXDOCCATALOGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Doccatalogo iddoccatalogo;
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
		name = "IDSACTICKET0",
		referencedColumnName = "IDXTICKET",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sacticket idsacticket;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNOTNOTIFICACION0",
		referencedColumnName = "IDXNOTIFICACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Notificacion idnotnotificacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iddocfichero" 
	)
	private List<Docrlinks> subdocrlinks; 

	public Doctipodocum getIddoctipodocum() {
		if(this.iddoctipodocum==null)this.iddoctipodocum=new org.suinsit.apps.document.Doctipodocum();
		  return this.iddoctipodocum; 
	}
	
	public Ssousuario getIdpropietario() {
		if(this.idpropietario==null)this.idpropietario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idpropietario; 
	}
	
	public Ssousuario getIdusuarioblock() {
		if(this.idusuarioblock==null)this.idusuarioblock=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusuarioblock; 
	}
	
	public Doccatalogo getIddoccatalogo() {
		if(this.iddoccatalogo==null)this.iddoccatalogo=new org.suinsit.apps.document.Doccatalogo();
		  return this.iddoccatalogo; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Sacticket getIdsacticket() {
		if(this.idsacticket==null)this.idsacticket=new org.suinsit.apps.asesor4.Sacticket();
		  return this.idsacticket; 
	}
	
	public Notificacion getIdnotnotificacion() {
		if(this.idnotnotificacion==null)this.idnotnotificacion=new org.suinsit.apps.asesor4.Notificacion();
		  return this.idnotnotificacion; 
	}
	
	public List<Docrlinks> getSubdocrlinks() {
		if(this.subdocrlinks==null)this.subdocrlinks=new ArrayList<>(0);
		  return this.subdocrlinks; 
	} 

}