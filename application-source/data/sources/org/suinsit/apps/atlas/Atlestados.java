package org.suinsit.apps.atlas;

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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.atlas.Atlproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLESTADOS" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLESTADOS",
	labelMonitor = "ESTADO",
	pk = "idxatlestados" 
)
public class Atlestados implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",bg-primary,bg-sucess,bg-info,bg-warning,bg-danger,bg-dark,bg-secondary,bg-light" 
		},
		message = "solamente admite lo valores: ,bg-primary,bg-sucess,bg-info,bg-warning,bg-danger,bg-dark,bg-secondary,bg-light" 
	)
	@Column (
		name = "bgcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String bgcolor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = true 
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
		name = "idxatlestados",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlestados;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatlestados" 
	)
	private List<Atlproject> subatlproject; 

	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	} 

}