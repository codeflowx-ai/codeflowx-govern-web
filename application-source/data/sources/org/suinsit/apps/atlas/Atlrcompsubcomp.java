package org.suinsit.apps.atlas;

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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLRCOMPSUBCOMP" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRCOMPSUBCOMP",
	labelMonitor = "",
	pk = "idxatlrcompsubcomp" 
)
public class Atlrcompsubcomp implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxatlrcompsubcomp",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrcompsubcomp;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLDEPEND0",
		referencedColumnName = "IDXATLCOMPONENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlcomponent idatldepend;
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

	public Atlcomponent getIdatldepend() {
		if(this.idatldepend==null)this.idatldepend=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatldepend; 
	}
	
	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	} 

}