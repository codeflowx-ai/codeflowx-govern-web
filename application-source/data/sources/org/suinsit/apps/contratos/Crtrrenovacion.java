package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.contratos.Crtmcontrato;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRTRRENOVACION" 
)
@Entidad (
	namespace = "contratos",
	type = "TABLE",
	name = "CRTRRENOVACION",
	labelMonitor = "",
	pk = "idxcrtrrenovacion" 
)
public class Crtrrenovacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcrtrrenovacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrtrrenovacion;
	@Column (
		name = "renovacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date renovacion;
	@Column (
		name = "vigente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean vigente;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRTMCONTRATO0",
		referencedColumnName = "IDXCRTMCONTRATO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crtmcontrato idcrtmcontrato; 

	public Crtmcontrato getIdcrtmcontrato() {
		if(this.idcrtmcontrato==null)this.idcrtmcontrato=new org.suinsit.apps.contratos.Crtmcontrato();
		  return this.idcrtmcontrato; 
	} 

}