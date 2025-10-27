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
import org.suinsit.apps.facturacin.Erpcomercial;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPEQUIPO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPEQUIPO",
	labelMonitor = "Equipo",
	pk = "idxerpequipo" 
)
public class Erpequipo implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "equipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String equipo;
	@Id
	@Column (
		name = "idxerpequipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpequipo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "objetivo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Objetivo facturación mes",
		type = "DECIMAL" 
	)
	private BigDecimal objetivo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDLIDER0",
		referencedColumnName = "IDXERPCOMERCIAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpcomercial idlider;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpequipo" 
	)
	private List<Erpcomercial> suberpcomercial; 

	public Erpcomercial getIdlider() {
		if(this.idlider==null)this.idlider=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.idlider; 
	}
	
	public List<Erpcomercial> getSuberpcomercial() {
		if(this.suberpcomercial==null)this.suberpcomercial=new ArrayList<>(0);
		  return this.suberpcomercial; 
	} 

}