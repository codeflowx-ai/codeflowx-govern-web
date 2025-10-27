package org.suinsit.apps.soporte;

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
import org.suinsit.apps.soporte.Sopmticket;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SOPMCRITICIDAD" 
)
@Entidad (
	namespace = "soporte",
	type = "TABLE",
	name = "SOPMCRITICIDAD",
	labelMonitor = "CRITICIDAD",
	pk = "idxsopmcriticidad" 
)
public class Sopmcriticidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ayudauser",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String ayudauser;
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
		max = 100 
	)
	@Column (
		name = "criticidad",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String criticidad;
	@Id
	@Column (
		name = "idxsopmcriticidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsopmcriticidad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsopmcriticidad" 
	)
	private List<Sopmticket> subsopmticket; 

	public List<Sopmticket> getSubsopmticket() {
		if(this.subsopmticket==null)this.subsopmticket=new ArrayList<>(0);
		  return this.subsopmticket; 
	} 

}