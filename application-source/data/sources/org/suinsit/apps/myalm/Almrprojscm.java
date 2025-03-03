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
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.myalm.Almscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMRPROJSCM" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRPROJSCM",
	pk = "idxalmrprojscm" 
)
public class Almrprojscm implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "email",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String email;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "grupouser",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String grupouser;
	@Id
	@Column (
		name = "idxalmrprojscm",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrprojscm;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "password",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String password;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proyecto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proyecto;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramabug",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramabug;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramadevelop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramadevelop;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramafeature",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramafeature;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramamaster",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramamaster;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramarelease",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramarelease;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ramaroot",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ramaroot;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "usuario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String usuario;
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
		name = "IDALMSCM0",
		referencedColumnName = "IDXALMSCM",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almscm idalmscm; 

	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Almscm getIdalmscm() {
		if(this.idalmscm==null)this.idalmscm=new org.suinsit.apps.myalm.Almscm();
		  return this.idalmscm; 
	} 

}