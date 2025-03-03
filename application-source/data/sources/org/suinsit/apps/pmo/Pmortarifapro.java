package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
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
import org.suinsit.apps.pmo.Pmomperfil;
import org.suinsit.apps.pmo.Pmomproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMORTARIFAPRO" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMORTARIFAPRO",
	labelMonitor = "",
	pk = "idxpmortarifapro" 
)
public class Pmortarifapro implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxpmortarifapro",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmortarifapro;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMPERFIL0",
		referencedColumnName = "IDXPMOMPERFIL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomperfil idpmomperfil;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMPROJECT0",
		referencedColumnName = "IDXPMOMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomproject idpmomproject; 

	public Pmomperfil getIdpmomperfil() {
		if(this.idpmomperfil==null)this.idpmomperfil=new org.suinsit.apps.pmo.Pmomperfil();
		  return this.idpmomperfil; 
	}
	
	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	} 

}