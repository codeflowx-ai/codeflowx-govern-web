package org.suinsit.apps.subvenciones;

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
import org.suinsit.apps.subvenciones.Submorganismo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBMTIPOORG" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "SUBMTIPOORG",
	labelMonitor = "Tipo",
	pk = "idxsubmtipoorg" 
)
public class Submtipoorg implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxsubmtipoorg",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubmtipoorg;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubmtipoorg" 
	)
	private List<Submorganismo> subsubmorganismo; 

	public List<Submorganismo> getSubsubmorganismo() {
		if(this.subsubmorganismo==null)this.subsubmorganismo=new ArrayList<>(0);
		  return this.subsubmorganismo; 
	} 

}