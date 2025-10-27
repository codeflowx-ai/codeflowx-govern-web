package org.suinsit.apps.partners;

import java.io.Serializable;
import java.lang.Long;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.partners.Partner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PTRUSERPARTNER" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "PTRUSERPARTNER",
	labelMonitor = "",
	pk = "idxptruserpartner" 
)
public class Ptruserpartner implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxptruserpartner",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxptruserpartner;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPARTNER0",
		referencedColumnName = "IDXPARTNER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Partner idpartner;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario; 

	public Partner getIdpartner() {
		if(this.idpartner==null)this.idpartner=new org.suinsit.apps.partners.Partner();
		  return this.idpartner; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}