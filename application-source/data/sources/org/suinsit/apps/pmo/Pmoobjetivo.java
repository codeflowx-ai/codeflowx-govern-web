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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.pmo.Pmomproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOOBJETIVO" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOOBJETIVO",
	labelMonitor = "",
	pk = "idxpmoobjetivo" 
)
public class Pmoobjetivo implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "objetivo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String objetivo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codobjetivo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "OB-01",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "PMOOBJETIVO_CODOBJETIVO",
		prefix = "",
		mask = "00000",
		addYear = false 
	)
	private String codobjetivo;
	@Id
	@Column (
		name = "idxpmoobjetivo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoobjetivo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoobjetivo" 
	)
	private List<Pmomproject> subpmomproject; 

	public List<Pmomproject> getSubpmomproject() {
		if(this.subpmomproject==null)this.subpmomproject=new ArrayList<>(0);
		  return this.subpmomproject; 
	} 

}