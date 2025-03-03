package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.suinsit.apps.expedientes.Gexmepediente;
import org.suinsit.apps.expedientes.Gexmtiporeq;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXRREQEXP" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXRREQEXP",
	labelMonitor = "requisito",
	pk = "idxgexrreqexp" 
)
public class Gexrreqexp implements Serializable { 

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
	@Column (
		name = "comencli",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comencli;
	@Column (
		name = "comentoffice",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentoffice;
	@Column (
		name = "cumplimentado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cumplimentado;
	@Column (
		name = "declaracion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String declaracion;
	@Id
	@Column (
		name = "idxgexrreqexp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexrreqexp;
	@Column (
		name = "obligatorio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean obligatorio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "requisito",
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
		name = "GEXRREQEXP_REQUISITO",
		prefix = "",
		mask = "0000000000",
		addYear = false 
	)
	private String requisito;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMTIPOREQ0",
		referencedColumnName = "IDXGEXMTIPOREQ",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmtiporeq idgexmtiporeq;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMEPEDIENTE0",
		referencedColumnName = "IDXGEXMEPEDIENTE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmepediente idgexmepediente;
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

	public Gexmtiporeq getIdgexmtiporeq() {
		if(this.idgexmtiporeq==null)this.idgexmtiporeq=new org.suinsit.apps.expedientes.Gexmtiporeq();
		  return this.idgexmtiporeq; 
	}
	
	public Gexmepediente getIdgexmepediente() {
		if(this.idgexmepediente==null)this.idgexmepediente=new org.suinsit.apps.expedientes.Gexmepediente();
		  return this.idgexmepediente; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}