package org.suinsit.apps.pmo;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.pmo.Pmorsuserproy;
import org.suinsit.apps.pmo.Pmortarifapro;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMPERFIL" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMPERFIL",
	labelMonitor = "",
	pk = "idxpmomperfil" 
)
public class Pmomperfil implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "externo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean externo;
	@Id
	@Column (
		name = "idxpmomperfil",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomperfil;
	@Column (
		name = "interno",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean interno;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "perfil",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String perfil;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomperfil" 
	)
	private List<Pmorsuserproy> subpmorsuserproy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomperfil" 
	)
	private List<Pmortarifapro> subpmortarifapro; 

	public List<Pmorsuserproy> getSubpmorsuserproy() {
		if(this.subpmorsuserproy==null)this.subpmorsuserproy=new ArrayList<>(0);
		  return this.subpmorsuserproy; 
	}
	
	public List<Pmortarifapro> getSubpmortarifapro() {
		if(this.subpmortarifapro==null)this.subpmortarifapro=new ArrayList<>(0);
		  return this.subpmortarifapro; 
	} 

}