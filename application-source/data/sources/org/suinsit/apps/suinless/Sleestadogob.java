package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.suinless.Slenivelgob;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLEESTADOGOB" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLEESTADOGOB",
	labelMonitor = "estado",
	pk = "idxsleestadogob" 
)
public class Sleestadogob implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "backcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String backcolor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 10 
	)
	@Column (
		name = "codestado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codestado;
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String description;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estado;
	@Id
	@Column (
		name = "idxsleestadogob",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleestadogob;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsleestadogob" 
	)
	private List<Slenivelgob> subslenivelgob;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsleestadogob" 
	)
	private List<Slprovider> subslprovider; 

	public List<Slenivelgob> getSubslenivelgob() {
		if(this.subslenivelgob==null)this.subslenivelgob=new ArrayList<>(0);
		  return this.subslenivelgob; 
	}
	
	public List<Slprovider> getSubslprovider() {
		if(this.subslprovider==null)this.subslprovider=new ArrayList<>(0);
		  return this.subslprovider; 
	} 

}