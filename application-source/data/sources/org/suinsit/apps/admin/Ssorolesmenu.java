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
import org.suinsit.apps.admin.Ssomenuitem;
import org.suinsit.apps.admin.Ssorol;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SSOROLESMENU" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "SSOROLESMENU",
	pk = "idxssorolesmenu" 
)
public class Ssorolesmenu implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxssorolesmenu",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxssorolesmenu;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOMENUITEM0",
		referencedColumnName = "IDXSSOMENUITEM",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssomenuitem idssomenuitem;
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

	public Ssomenuitem getIdssomenuitem() {
		if(this.idssomenuitem==null)this.idssomenuitem=new org.suinsit.apps.admin.Ssomenuitem();
		  return this.idssomenuitem; 
	}
	
	public Ssorol getIdssorol() {
		if(this.idssorol==null)this.idssorol=new org.suinsit.apps.admin.Ssorol();
		  return this.idssorol; 
	} 

}