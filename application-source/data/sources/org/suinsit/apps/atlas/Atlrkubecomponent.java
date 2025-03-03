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
import org.suinsit.apps.atlas.Atlkubecloud;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLRKUBECOMPONENT" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRKUBECOMPONENT",
	labelMonitor = "",
	pk = "idxatlrkubecomponent" 
)
public class Atlrkubecomponent implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxatlrkubecomponent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrkubecomponent;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLKUBECLOUD0",
		referencedColumnName = "IDXATLKUBECLOUD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlkubecloud idatlkubecloud;
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

	public Atlkubecloud getIdatlkubecloud() {
		if(this.idatlkubecloud==null)this.idatlkubecloud=new org.suinsit.apps.atlas.Atlkubecloud();
		  return this.idatlkubecloud; 
	}
	
	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	} 

}