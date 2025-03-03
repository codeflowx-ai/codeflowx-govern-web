package org.suinsit.apps.admin;

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
import org.suinsit.apps.admin.Aplicacion;
import org.suinsit.apps.admin.Ssousuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSORUSERAPP" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSORUSERAPP",
	pk = "idxssoruserapp" 
)
public class Ssoruserapp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxssoruserapp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssoruserapp;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOAPLICACION0",
		referencedColumnName = "IDXAPLICACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Aplicacion idssoaplicacion;
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

	public Aplicacion getIdssoaplicacion() {
		if(this.idssoaplicacion==null)this.idssoaplicacion=new org.suinsit.apps.admin.Aplicacion();
		  return this.idssoaplicacion; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}