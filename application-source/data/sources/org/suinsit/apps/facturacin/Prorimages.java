package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PRORIMAGES" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "PRORIMAGES",
	labelMonitor = "",
	pk = "idxprorimages" 
)
public class Prorimages implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "imagen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object imagen;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tooltip",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tooltip;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "src",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String src;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "widht",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String widht;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "height",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String height;
	@Id
	@Column (
		name = "idxprorimages",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxprorimages;
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

	public Promproducto getIdpromproducto() {
		if(this.idpromproducto==null)this.idpromproducto=new org.suinsit.apps.facturacin.Promproducto();
		  return this.idpromproducto; 
	} 

}