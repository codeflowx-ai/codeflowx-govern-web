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
import org.suinsit.apps.atlas.Atldeployment;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLRCOMPDEPLOY" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRCOMPDEPLOY",
	labelMonitor = "",
	pk = "idxatlrcompdeploy" 
)
public class Atlrcompdeploy implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxatlrcompdeploy",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrcompdeploy;
	private boolean updatable;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLDEPLOYMENT0",
		referencedColumnName = "IDXATLDEPLOYMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atldeployment idatldeployment; 

	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	}
	
	public Atldeployment getIdatldeployment() {
		if(this.idatldeployment==null)this.idatldeployment=new org.suinsit.apps.atlas.Atldeployment();
		  return this.idatldeployment; 
	} 

}