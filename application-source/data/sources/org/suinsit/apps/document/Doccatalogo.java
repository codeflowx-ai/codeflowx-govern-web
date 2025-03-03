package org.suinsit.apps.document;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Mmodulo;
import org.suinsit.apps.document.Docfichero;
import org.suinsit.apps.document.Doctipodocum;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "DOCCATALOGO" 
)
@Entidad (
	namespace = "document",
	type = "TABLE",
	name = "DOCCATALOGO",
	pk = "idxdoccatalogo" 
)
public class Doccatalogo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "accesocli",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean accesocli;
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
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Anual,Trimestral,Semestral,Mensual" 
		},
		message = "solamente admite lo valores: ,Anual,Trimestral,Semestral,Mensual" 
	)
	@Column (
		name = "agrupar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String agrupar;
	@Size (
		min = 0,
		max = 300 
	)
	@Column (
		name = "carpeta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String carpeta;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "catalogo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Catalogo",
		type = "VARCHAR" 
	)
	private String catalogo;
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
		label = "Catalogo",
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
		label = "Catalogo",
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "groupclient",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean groupclient;
	@Column (
		name = "groupuser",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean groupuser;
	@Id
	@Column (
		name = "idxdoccatalogo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxdoccatalogo;
	@Column (
		name = "notificar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean notificar;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 500 
	)
	@Column (
		name = "ruta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ruta;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMMODULO0",
		referencedColumnName = "IDXMMODULO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mmodulo idmmodulo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iddoccatalogo" 
	)
	private List<Docfichero> subdocfichero;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iddoccatalogo" 
	)
	private List<Doctipodocum> subdoctipodocum; 

	public Mmodulo getIdmmodulo() {
		if(this.idmmodulo==null)this.idmmodulo=new org.suinsit.apps.admin.Mmodulo();
		  return this.idmmodulo; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	}
	
	public List<Doctipodocum> getSubdoctipodocum() {
		if(this.subdoctipodocum==null)this.subdoctipodocum=new ArrayList<>(0);
		  return this.subdoctipodocum; 
	} 

}