package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	name = "ATLRCOMPONVER" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRCOMPONVER",
	labelMonitor = "version",
	pk = "idxatlrcomponver" 
)
public class Atlrcomponver implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "release",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String release;
	@Column (
		name = "deploymentyml",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String deploymentyml;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "imagen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String imagen;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	@Id
	@Column (
		name = "idxatlrcomponver",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrcomponver;
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

	public Atlcomponent getIdatlcomponent() {
		if(this.idatlcomponent==null)this.idatlcomponent=new org.suinsit.apps.atlas.Atlcomponent();
		  return this.idatlcomponent; 
	} 

}