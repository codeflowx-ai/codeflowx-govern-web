package org.suinsit.apps.myalm;

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
import org.suinsit.apps.myalm.Almenviroment;
import org.suinsit.apps.myalm.Almproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMRPROJENV" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRPROJENV",
	pk = "idxalmrprojenv" 
)
public class Almrprojenv implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxalmrprojenv",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrprojenv;
	private boolean updatable;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMENVIROMENT0",
		referencedColumnName = "IDXALMENVIROMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almenviroment idalmenviroment; 

	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Almenviroment getIdalmenviroment() {
		if(this.idalmenviroment==null)this.idalmenviroment=new org.suinsit.apps.myalm.Almenviroment();
		  return this.idalmenviroment; 
	} 

}