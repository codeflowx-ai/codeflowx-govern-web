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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Sltemplate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLTEMPLATEVAR" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLTEMPLATEVAR",
	labelMonitor = "NAME",
	pk = "idxsltemplatevar" 
)
public class Sltemplatevar implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "validationregex",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String validationregex;
	@Column (
		name = "requerido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean requerido;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "defaultvalue",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String defaultvalue;
	@Size (
		min = 0,
		max = 1000 
	)
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String description;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "name",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String name;
	@Id
	@Column (
		name = "idxsltemplatevar",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsltemplatevar;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLTEMPLATE0",
		referencedColumnName = "IDXSLTEMPLATE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sltemplate idsltemplate; 

	public Sltemplate getIdsltemplate() {
		if(this.idsltemplate==null)this.idsltemplate=new org.suinsit.apps.suinless.Sltemplate();
		  return this.idsltemplate; 
	} 

}