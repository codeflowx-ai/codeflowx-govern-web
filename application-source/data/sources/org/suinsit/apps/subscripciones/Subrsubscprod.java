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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.subscripciones.Periodicidad;
import org.suinsit.apps.subscripciones.Subscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBRSUBSCPROD" 
)
@Entidad (
	namespace = "subscripciones",
	type = "TABLE",
	name = "SUBRSUBSCPROD",
	labelMonitor = "PRODUCTO",
	pk = "idxsubrsubscprod" 
)
public class Subrsubscprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "articulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String articulo;
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
	@NotNull
	@NotBlank
	@Column (
		name = "desde",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date desde;
	@NotNull
	@NotBlank
	@Column (
		name = "hasta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date hasta;
	@Id
	@Column (
		name = "idxsubrsubscprod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubrsubscprod;
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
	@Size (
		min = 0,
		max = 100 
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
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "SUBRSUBSCPROD_LICENCIA",
		prefix = "LIN",
		mask = "00000",
		addYear = true 
	)
	private String licencia;
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
		name = "IDPROMCATEGORIA0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idpromcategoria;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPROMPRODUCTO0",
		referencedColumnName = "IDXPROMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promproducto idpromproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPERIODICIDAD0",
		referencedColumnName = "IDXPERIODICIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Periodicidad idperiodicidad; 

	public Subscripcion getIdsubscripcion() {
		if(this.idsubscripcion==null)this.idsubscripcion=new org.suinsit.apps.subscripciones.Subscripcion();
		  return this.idsubscripcion; 
	}
	
	public Promcategoria getIdpromcategoria() {
		if(this.idpromcategoria==null)this.idpromcategoria=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idpromcategoria; 
	}
	
	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Periodicidad getIdperiodicidad() {
		if(this.idperiodicidad==null)this.idperiodicidad=new org.suinsit.apps.subscripciones.Periodicidad();
		  return this.idperiodicidad; 
	} 

}