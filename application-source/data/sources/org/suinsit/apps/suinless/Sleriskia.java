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
import org.suinsit.apps.suinless.Slenivelrisk;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLERISKIA" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLERISKIA",
	labelMonitor = "TITULO",
	pk = "idxsleriskia" 
)
public class Sleriskia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "descbreve",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descbreve;
	@Id
	@Column (
		name = "idxsleriskia",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleriskia;
	@Column (
		name = "infopublic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infopublic;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "titulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String titulo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLENIVELRISK0",
		referencedColumnName = "IDXSLENIVELRISK",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slenivelrisk idslenivelrisk; 

	public Slenivelrisk getIdslenivelrisk() {
		if(this.idslenivelrisk==null)this.idslenivelrisk=new org.suinsit.apps.suinless.Slenivelrisk();
		  return this.idslenivelrisk; 
	} 

}