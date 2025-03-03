package org.suinsit.apps.suinless;

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
import org.suinsit.apps.suinless.Slespromp;
import org.suinsit.apps.suinless.Slestypevar;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLERVAR" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLERVAR",
	labelMonitor = "etiqueta",
	pk = "idxslervar" 
)
public class Slervar implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "required",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean required;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "etiqueta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String etiqueta;
	@Id
	@Column (
		name = "idxslervar",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslervar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "variable",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String variable;
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
		name = "IDSLESTYPEVAR0",
		referencedColumnName = "IDXSLESTYPEVAR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slestypevar idslestypevar; 

	public Slespromp getIdslespromp() {
		if(this.idslespromp==null)this.idslespromp=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslespromp; 
	}
	
	public Slestypevar getIdslestypevar() {
		if(this.idslestypevar==null)this.idslestypevar=new org.suinsit.apps.suinless.Slestypevar();
		  return this.idslestypevar; 
	} 

}