package org.suinsit.apps.facturacli;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacli.Clilineafactura;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CLIPRODUCTO" 
)
@Entidad (
	namespace = "facturacli",
	type = "TABLE",
	name = "CLIPRODUCTO",
	labelMonitor = "PRODUCTO",
	pk = "idxcliproducto" 
)
public class Cliproducto implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "idxcliproducto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcliproducto;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "preciocoste",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal preciocoste;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precioventa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal precioventa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "producto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String producto;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcliproducto" 
	)
	private List<Clilineafactura> subclilineafactura; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Clilineafactura> getSubclilineafactura() {
		if(this.subclilineafactura==null)this.subclilineafactura=new ArrayList<>(0);
		  return this.subclilineafactura; 
	} 

}