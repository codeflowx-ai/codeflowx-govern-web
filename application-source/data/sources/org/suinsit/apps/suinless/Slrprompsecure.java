package org.suinsit.apps.suinless;

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
import org.suinsit.apps.suinless.Slespromp;
import org.suinsit.apps.suinless.Slsecuritypromt;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRPROMPSECURE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRPROMPSECURE",
	pk = "idxslrprompsecure" 
)
public class Slrprompsecure implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslrprompsecure",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslrprompsecure;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLSECURITYPROMT0",
		referencedColumnName = "IDXSLSECURITYPROMT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slsecuritypromt idslsecuritypromt;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESPROMP0",
		referencedColumnName = "IDXSLESPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slespromp idslespromp; 

	public Slsecuritypromt getIdslsecuritypromt() {
		if(this.idslsecuritypromt==null)this.idslsecuritypromt=new org.suinsit.apps.suinless.Slsecuritypromt();
		  return this.idslsecuritypromt; 
	}
	
	public Slespromp getIdslespromp() {
		if(this.idslespromp==null)this.idslespromp=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslespromp; 
	} 

}