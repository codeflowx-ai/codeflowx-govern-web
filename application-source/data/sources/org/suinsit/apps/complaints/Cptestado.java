package org.suinsit.apps.complaints;

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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.complaints.Cptcomplaint;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTESTADO" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTESTADO",
	labelMonitor = "estado",
	pk = "idxcptestado" 
)
public class Cptestado implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",primary,secondary,success,danger,warning,info,dark,light" 
		},
		message = "solamente admite lo valores: ,primary,secondary,success,danger,warning,info,dark,light" 
	)
	@Column (
		name = "bgcolor",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String bgcolor;
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
		name = "idxcptestado",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptestado;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcptestado" 
	)
	private List<Cptcomplaint> subcptcomplaint; 

	public List<Cptcomplaint> getSubcptcomplaint() {
		if(this.subcptcomplaint==null)this.subcptcomplaint=new ArrayList<>(0);
		  return this.subcptcomplaint; 
	} 

}