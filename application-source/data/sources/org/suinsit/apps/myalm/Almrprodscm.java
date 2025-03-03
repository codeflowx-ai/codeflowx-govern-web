package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.myalm.Almenviroment;
import org.suinsit.apps.myalm.Almproduct;
import org.suinsit.apps.myalm.Almscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMRPRODSCM" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRPRODSCM",
	labelMonitor = "Componente",
	pk = "idxalmrprodscm" 
)
public class Almrprodscm implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "branch",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String branch;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "componente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String componente;
	@Id
	@Column (
		name = "idxalmrprodscm",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrprodscm;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "repositorio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String repositorio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "templatename",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String templatename;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMPRODUCT0",
		referencedColumnName = "IDXALMPRODUCT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almproduct idalmproduct;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMSCM0",
		referencedColumnName = "IDXALMSCM",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almscm idalmscm;
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

	public Almproduct getIdalmproduct() {
		if(this.idalmproduct==null)this.idalmproduct=new org.suinsit.apps.myalm.Almproduct();
		  return this.idalmproduct; 
	}
	
	public Almscm getIdalmscm() {
		if(this.idalmscm==null)this.idalmscm=new org.suinsit.apps.myalm.Almscm();
		  return this.idalmscm; 
	}
	
	public Almenviroment getIdalmenviroment() {
		if(this.idalmenviroment==null)this.idalmenviroment=new org.suinsit.apps.myalm.Almenviroment();
		  return this.idalmenviroment; 
	} 

}