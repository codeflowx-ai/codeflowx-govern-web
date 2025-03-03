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
import org.suinsit.apps.atlas.Atlcomponent;
import org.suinsit.apps.myalm.Almproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMRCOMP" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRCOMP",
	pk = "idxalmrcomp" 
)
public class Almrcomp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxalmrcomp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrcomp;
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
		name = "IDATLCOMPONENT0",
		referencedColumnName = "IDXATLCOMPONENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlcomponent idatlcomponent; 

	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	} 

}