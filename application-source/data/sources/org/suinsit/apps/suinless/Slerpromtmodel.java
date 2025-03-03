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
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slespromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLERPROMTMODEL" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLERPROMTMODEL",
	labelMonitor = "",
	pk = "idxslerpromtmodel" 
)
public class Slerpromtmodel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslerpromtmodel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslerpromtmodel;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL0",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel;
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

	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Slespromp getIdslespromp() {
		if(this.idslespromp==null)this.idslespromp=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslespromp; 
	} 

}