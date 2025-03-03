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
import org.suinsit.apps.suinless.Slespromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRPROJETPROMT" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRPROJETPROMT",
	pk = "idxslrprojetpromt" 
)
public class Slrprojetpromt implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslrprojetpromt",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslrprojetpromt;
	private boolean updatable;
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

	public Slespromp getIdslespromp() {
		if(this.idslespromp==null)this.idslespromp=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslespromp; 
	}
	
	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	} 

}