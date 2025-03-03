package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Sltemplate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLMCATEGORIA" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLMCATEGORIA",
	labelMonitor = "CATEGORIA",
	pk = "idxslmcategoria" 
)
public class Slmcategoria implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 10 
	)
	@Column (
		name = "alias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String alias;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "categoria",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String categoria;
	@Id
	@Column (
		name = "idxslmcategoria",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslmcategoria;
	@Column (
		name = "rag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean rag;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmcategoria" 
	)
	private List<Slesmodel> subslesmodel;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmcategoria" 
	)
	private List<Sltemplate> subsltemplate; 

	public List<Slesmodel> getSubslesmodel() {
		if(this.subslesmodel==null)this.subslesmodel=new ArrayList<>(0);
		  return this.subslesmodel; 
	}
	
	public List<Sltemplate> getSubsltemplate() {
		if(this.subsltemplate==null)this.subsltemplate=new ArrayList<>(0);
		  return this.subsltemplate; 
	} 

}