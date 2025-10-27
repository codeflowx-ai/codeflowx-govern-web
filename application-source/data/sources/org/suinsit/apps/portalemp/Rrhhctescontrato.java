package org.suinsit.apps.portalemp;

import java.io.Serializable;
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
import org.suinsit.apps.portalemp.Rrhmtipoconta;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHCTESCONTRATO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHCTESCONTRATO",
	pk = "idxrrhhctescontrato" 
)
public class Rrhhctescontrato implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "cteempleado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cteempleado;
	@Column (
		name = "cteempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cteempresa;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "porcentaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal porcentaje;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "concepto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String concepto;
	@Id
	@Column (
		name = "idxrrhhctescontrato",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhctescontrato;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHMTIPOCONTA0",
		referencedColumnName = "IDXRRHMTIPOCONTA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhmtipoconta idrrhmtipoconta; 

	public Rrhmtipoconta getIdrrhmtipoconta() {
		if(this.idrrhmtipoconta==null)this.idrrhmtipoconta=new org.suinsit.apps.portalemp.Rrhmtipoconta();
		  return this.idrrhmtipoconta; 
	} 

}