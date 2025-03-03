package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.subvenciones.Kitrbonocat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITRBONOCATPROD" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITRBONOCATPROD",
	labelMonitor = "",
	pk = "idxkitrbonocatprod" 
)
public class Kitrbonocatprod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
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
	@Id
	@Column (
		name = "idxkitrbonocatprod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitrbonocatprod;
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
	@Column (
		name = "unidades",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer unidades;
	private boolean updatable;
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
		name = "IDKITRBONOCAT0",
		referencedColumnName = "IDXKITRBONOCAT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitrbonocat idkitrbonocat; 

	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	}
	
	public Kitrbonocat getIdkitrbonocat() {
		if(this.idkitrbonocat==null)this.idkitrbonocat=new org.suinsit.apps.subvenciones.Kitrbonocat();
		  return this.idkitrbonocat; 
	} 

}