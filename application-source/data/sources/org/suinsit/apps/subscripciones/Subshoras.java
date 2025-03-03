package org.suinsit.apps.subscripciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.admin.Mmotivohora;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBSHORAS" 
)
@Entidad (
	namespace = "subscripciones",
	type = "TABLE",
	name = "SUBSHORAS",
	labelMonitor = "concepto",
	pk = "idxsubshoras" 
)
public class Subshoras implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "concepto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String concepto;
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
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Id
	@Column (
		name = "idxsubshoras",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubshoras;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "tiempo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal tiempo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBSCRIPCION0",
		referencedColumnName = "IDXSUBSCRIPCION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subscripcion idsubscripcion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMMOTIVOHORA0",
		referencedColumnName = "IDXMMOTIVOHORA",
		nullable = false,
		insertable = true,
		updatable = true 
	)
	private Mmotivohora idmmotivohora; 

	public Subscripcion getIdsubscripcion() {
		if(this.idsubscripcion==null)this.idsubscripcion=new org.suinsit.apps.subscripciones.Subscripcion();
		  return this.idsubscripcion; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Mmotivohora getIdmmotivohora() {
		if(this.idmmotivohora==null)this.idmmotivohora=new org.suinsit.apps.admin.Mmotivohora();
		  return this.idmmotivohora; 
	} 

}