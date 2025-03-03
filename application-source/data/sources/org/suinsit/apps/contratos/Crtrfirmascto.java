package org.suinsit.apps.contratos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.sql.Timestamp;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.contratos.Crtmcontrato;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRTRFIRMASCTO" 
)
@Entidad (
	namespace = "contratos",
	type = "TABLE",
	name = "CRTRFIRMASCTO",
	labelMonitor = "FECHAFIRMA",
	pk = "idxcrtrfirmascto" 
)
public class Crtrfirmascto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "otpfirma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String otpfirma;
	@Column (
		name = "firmabio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object firmabio;
	@Column (
		name = "fechafirma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechafirma;
	@Id
	@Column (
		name = "idxcrtrfirmascto",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrtrfirmascto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String couseralta;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDUSUARIOFIRMA0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusuariofirma; 

	public Crtmcontrato getIdcrtmcontrato() {
		if(this.idcrtmcontrato==null)this.idcrtmcontrato=new org.suinsit.apps.contratos.Crtmcontrato();
		  return this.idcrtmcontrato; 
	}
	
	public Ssousuario getIdusuariofirma() {
		if(this.idusuariofirma==null)this.idusuariofirma=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusuariofirma; 
	} 

}