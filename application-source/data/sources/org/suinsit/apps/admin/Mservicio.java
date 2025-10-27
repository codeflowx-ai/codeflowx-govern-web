package org.suinsit.apps.admin;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.marketing.Mktmrrss;
import org.suinsit.apps.marketing.Mktprovmail;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MSERVICIO" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MSERVICIO",
	labelMonitor = "",
	pk = "idxmservicio" 
)
public class Mservicio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clasejava",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clasejava;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Id
	@Column (
		name = "idxmservicio",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmservicio;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "MSERVICIO_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = false 
	)
	private String referencia;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "servicioext",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String servicioext;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmservicio" 
	)
	private List<Mktmrrss> submktmrrss;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmservicio" 
	)
	private List<Mktprovmail> submktprovmail; 

	public List<Mktmrrss> getSubmktmrrss() {
		if(this.submktmrrss==null)this.submktmrrss=new ArrayList<>(0);
		  return this.submktmrrss; 
	}
	
	public List<Mktprovmail> getSubmktprovmail() {
		if(this.submktprovmail==null)this.submktprovmail=new ArrayList<>(0);
		  return this.submktprovmail; 
	} 

}