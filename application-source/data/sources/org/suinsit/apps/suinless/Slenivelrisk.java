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
import org.suinsit.apps.suinless.Sleriskia;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLENIVELRISK" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLENIVELRISK",
	labelMonitor = "NIVEL",
	pk = "idxslenivelrisk" 
)
public class Slenivelrisk implements Serializable { 

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
	@Id
	@Column (
		name = "idxslenivelrisk",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslenivelrisk;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nivel",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nivel;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslenivelrisk" 
	)
	private List<Sleriskia> subsleriskia; 

	public List<Sleriskia> getSubsleriskia() {
		if(this.subsleriskia==null)this.subsleriskia=new ArrayList<>(0);
		  return this.subsleriskia; 
	} 

}