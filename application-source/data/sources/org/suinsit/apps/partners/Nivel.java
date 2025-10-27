package org.suinsit.apps.partners;

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
import org.suinsit.apps.partners.Partner;
import org.suinsit.apps.partners.Tipopartner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NIVEL" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "NIVEL",
	labelMonitor = "NIVEL",
	pk = "idxnivel" 
)
public class Nivel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "comisionmax",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal comisionmax;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "comisionmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal comisionmin;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "objetivos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal objetivos;
	@Column (
		name = "certificaciones",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String certificaciones;
	@Column (
		name = "requisitos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String requisitos;
	@Id
	@Column (
		name = "idxnivel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnivel;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nivel",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nivel;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnivel" 
	)
	private List<Tipopartner> subtipopartner;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnivel" 
	)
	private List<Partner> subpartner; 

	public List<Tipopartner> getSubtipopartner() {
		if(this.subtipopartner==null)this.subtipopartner=new ArrayList<>(0);
		  return this.subtipopartner; 
	}
	
	public List<Partner> getSubpartner() {
		if(this.subpartner==null)this.subpartner=new ArrayList<>(0);
		  return this.subpartner; 
	} 

}