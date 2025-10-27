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
import org.suinsit.apps.subscripciones.Subshoras;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MMOTIVOHORA" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MMOTIVOHORA",
	labelMonitor = "MOTIVO",
	pk = "idxmmotivohora" 
)
public class Mmotivohora implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "nofacturable",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean nofacturable;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "motivo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String motivo;
	@Id
	@Column (
		name = "idxmmotivohora",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmmotivohora;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmmotivohora" 
	)
	private List<Subshoras> subsubshoras; 

	public List<Subshoras> getSubsubshoras() {
		if(this.subsubshoras==null)this.subsubshoras=new ArrayList<>(0);
		  return this.subsubshoras; 
	} 

}