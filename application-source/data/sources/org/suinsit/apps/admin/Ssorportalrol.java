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
import org.suinsit.apps.admin.Ssoportal;
import org.suinsit.apps.admin.Ssorol;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSORPORTALROL" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSORPORTALROL",
	pk = "idxssorportalrol" 
)
public class Ssorportalrol implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxssorportalrol",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssorportalrol;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOROL0",
		referencedColumnName = "IDXSSOROL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssorol idssorol;
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

	public Ssorol getIdssorol() {
		if(this.idssorol==null)this.idssorol=new org.suinsit.apps.admin.Ssorol();
		  return this.idssorol; 
	}
	
	public Ssoportal getIdssoportal() {
		if(this.idssoportal==null)this.idssoportal=new org.suinsit.apps.admin.Ssoportal();
		  return this.idssoportal; 
	} 

}