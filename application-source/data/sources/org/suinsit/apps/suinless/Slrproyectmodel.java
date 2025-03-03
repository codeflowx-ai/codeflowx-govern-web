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
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRPROYECTMODEL" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRPROYECTMODEL",
	pk = "idxslrproyectmodel" 
)
public class Slrproyectmodel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslrproyectmodel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslrproyectmodel;
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
		name = "IDSLPROVIDER0",
		referencedColumnName = "IDXSLPROVIDER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slprovider idslprovider;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMPROJECT0",
		referencedColumnName = "IDXALMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almproject idalmproject; 

	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Slprovider getIdslprovider() {
		if(this.idslprovider==null)this.idslprovider=new org.suinsit.apps.suinless.Slprovider();
		  return this.idslprovider; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	} 

}