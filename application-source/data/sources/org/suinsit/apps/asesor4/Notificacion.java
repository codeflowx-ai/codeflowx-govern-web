package org.suinsit.apps.asesor4;

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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.document.Docfichero;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NOTNOTIFICACION" 
)
@Entidad (
	namespace = "asesor4",
	type = "TABLE",
	name = "NOTNOTIFICACION",
	pk = "idxnotificacion" 
)
public class Notificacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estilocss",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estilocss;
	@Column (
		name = "leido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp leido;
	@Column (
		name = "enviado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp enviado;
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
	@Column (
		name = "mensaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String mensaje;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "asunto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String asunto;
	@Id
	@Column (
		name = "idxnotificacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotificacion;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSUENVIA0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusuenvia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSUDESTINO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusudestino;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnotnotificacion" 
	)
	private List<Docfichero> subdocfichero; 

	public Ssousuario getIdusuenvia() {
		if(this.idusuenvia==null)this.idusuenvia=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusuenvia; 
	}
	
	public Ssousuario getIdusudestino() {
		if(this.idusudestino==null)this.idusudestino=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusudestino; 
	}
	
	public List<Docfichero> getSubdocfichero() {
		if(this.subdocfichero==null)this.subdocfichero=new ArrayList<>(0);
		  return this.subdocfichero; 
	} 

}