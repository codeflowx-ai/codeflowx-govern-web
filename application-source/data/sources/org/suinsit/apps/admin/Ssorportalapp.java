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
import org.suinsit.apps.admin.Ssoportal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSORPORTALAPP" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSORPORTALAPP",
	pk = "idxssorportalapp" 
)
public class Ssorportalapp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxssorportalapp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssorportalapp;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOPORTAL0",
		referencedColumnName = "IDXSSOPORTAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssoportal idssoportal;
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

	public Ssoportal getIdssoportal() {
		if(this.idssoportal==null)this.idssoportal=new org.suinsit.apps.admin.Ssoportal();
		  return this.idssoportal; 
	}
	
	public Aplicacion getIdssoaplicacion() {
		if(this.idssoaplicacion==null)this.idssoaplicacion=new org.suinsit.apps.admin.Aplicacion();
		  return this.idssoaplicacion; 
	} 

}