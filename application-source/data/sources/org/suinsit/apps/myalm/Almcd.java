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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMCD" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMCD",
	labelMonitor = "name",
	pk = "idxalmcd" 
)
public class Almcd implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "desxripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String desxripcion;
	@Id
	@Column (
		name = "idxalmcd",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmcd;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "name",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String name;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "passkey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String passkey;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlconsole",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlconsole;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urldeploy",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urldeploy;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "username",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String username;
	private boolean updatable;
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

	public Almenviroment getIdalmenviroment() {
		if(this.idalmenviroment==null)this.idalmenviroment=new org.suinsit.apps.myalm.Almenviroment();
		  return this.idalmenviroment; 
	} 

}