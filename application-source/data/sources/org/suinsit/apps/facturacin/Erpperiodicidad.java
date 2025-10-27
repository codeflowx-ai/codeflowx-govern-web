package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
import org.suinsit.apps.facturacin.Promproducto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPPERIODICIDAD" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPPERIODICIDAD",
	pk = "idxerpperiodicidad" 
)
public class Erpperiodicidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "coeficiente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Coeficiente %",
		type = "DECIMAL" 
	)
	private BigDecimal coeficiente;
	@Id
	@Column (
		name = "idxerpperiodicidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpperiodicidad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "periodicidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String periodicidad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpperiodicidad" 
	)
	private List<Promproducto> subpromproducto; 

	public List<Promproducto> getSubpromproducto() {
		if(this.subpromproducto==null)this.subpromproducto=new ArrayList<>(0);
		  return this.subpromproducto; 
	} 

}